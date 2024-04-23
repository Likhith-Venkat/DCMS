package com.example.DCMS.services;

import com.example.DCMS.DTOs.approveDTO;
import com.example.DCMS.DTOs.dataObjectDTO;
import com.example.DCMS.DTOs.rejectDTO;
import com.example.DCMS.exception.AlreadyExistsException;
import com.example.DCMS.exception.ResourceNotFoundException;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ObjectService implements ObjectServiceInterface {

    @Autowired
    private dataObjectRepo dor;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = Logger.getLogger("ObjectController.class");

    static final String APPROVED = "APPROVED";
    static final String REJECTED = "REJECTED";
    static final String PENDING = "PENDING";

    @Override
    public ResponseEntity<String> approveObject(approveDTO req, HttpServletRequest servletRequest) {
        LOGGER.info("Executing 'approve' by checker");
        String id = req.getId();
        String url = req.getUrl();
        String methodType = req.getMethod();
        Optional<dataObject> co = Optional.ofNullable(dor.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Object cannot be found.")));

        dataObject currentObject = co.get();
        if (!Objects.equals(currentObject.getStatus(), PENDING))
            throw new AlreadyExistsException("Object Already checked");

        currentObject.setStatus(APPROVED);
        dataObject savedObject = currentObject;

        Object payload = savedObject.getData();
        HttpHeaders headers = new HttpHeaders();

        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!("content-type".equalsIgnoreCase(headerName) || "X-TENANT-ID".equalsIgnoreCase(headerName))) {
                continue;
            }
            String headerValue = servletRequest.getHeader(headerName);
            headers.add(headerName, headerValue);
        }
        HttpMethod httpMethod = switch (methodType.toUpperCase()) {
            case "POST" -> HttpMethod.POST;
            case "GET" -> HttpMethod.GET;
            case "PUT" -> HttpMethod.PUT;
            default ->
                // Handle invalid method type
                    throw new IllegalArgumentException("Invalid HTTP method type: " + methodType);
        };

        String stringPayload = null;
        try {
            stringPayload = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(stringPayload, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    httpMethod,
                    requestEntity,
                    String.class
            );

            // Handle the case where the response status code is 400
            if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                savedObject.setStatus(REJECTED);
                savedObject.setRejectReason(responseEntity.toString());
            } else {
                savedObject.setStatus(APPROVED);
            }
            savedObject.validateBeforeSave();
            dor.save(savedObject);
            LOGGER.info("Executed 'approve' by checker");
            return responseEntity;

        } catch (HttpClientErrorException e) {
            // Handle the case where the response status code is 400
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                savedObject.setStatus(REJECTED);
                savedObject.setRejectReason(e.getResponseBodyAsString());
            } else {
                savedObject.setStatus(APPROVED);
            }
            savedObject.validateBeforeSave();
            dor.save(savedObject);
            LOGGER.info("Error occurred while executing 'approve' by checker");
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Override
    public ResponseEntity<dataObject> rejectObject(rejectDTO req) {
        LOGGER.info("Executing 'reject' by checker");
        String id = req.getId();
        String rejectReason = req.getRejectReason();
        Optional<dataObject> co = Optional.of(dor.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Object Not Found")));
        dataObject currentObject = co.get();
        if (!Objects.equals(currentObject.getStatus(), PENDING))
            throw new AlreadyExistsException("Object Already checked");
        currentObject.setStatus(REJECTED);
        currentObject.setRejectReason(rejectReason);
        currentObject.validateBeforeSave();
        dataObject savedObject = dor.save(currentObject);
        LOGGER.info("Executed 'reject' by checker");
        return new ResponseEntity<>(savedObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<dataObject> addObject(dataObjectDTO req) {
        LOGGER.info("Executing 'add' by maker");
        req.setStatus(PENDING);
        req.setObjectType(req.getObjectType().toUpperCase());
        req.setId(req.getUniqueName() + req.getObjectType());
        dataObject currentObject = dataObject.builder()
                .data(req.getData())
                .username(req.getUsername())
                .userEmail(req.getUserEmail())
                .objectType(req.getObjectType())
                .status(req.getStatus())
                .id(req.getId())
                .createdDate(req.getCreatedDate())
                .uniqueName(req.getUniqueName())
                .build();
        Optional<dataObject> chk = dor.findById(req.getId());
        if (chk.isPresent()) {
            throw new AlreadyExistsException("Object Already exists");
        }
        currentObject.validateBeforeSave();
        dataObject savedObject = dor.save(currentObject);
        LOGGER.info("Executed 'add' by maker");
        return new ResponseEntity<>(savedObject, HttpStatus.CREATED);
    }
}
