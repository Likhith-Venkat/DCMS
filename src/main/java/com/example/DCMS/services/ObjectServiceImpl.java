package com.example.DCMS.services;

import com.example.DCMS.DTOs.ApproveDTO;
import com.example.DCMS.DTOs.DataObjectDTO;
import com.example.DCMS.DTOs.RejectDTO;
import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.enums.Status;
import com.example.DCMS.exception.AlreadyExistsException;
import com.example.DCMS.exception.ResourceNotFoundException;
import com.example.DCMS.models.DataObject;
import com.example.DCMS.repositories.DataObjectRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ObjectServiceImpl implements ObjectService {

    @Autowired
    private DataObjectRepo dor;

    @Autowired
    private ObjectMapper objectMapper;

    RestTemplate restTemplate = new RestTemplate();

    private static final Logger LOGGER = Logger.getLogger("ObjectController.class");




    @Override
    public DataObject approveObject(ApproveDTO req, HttpHeaders headers) {
        LOGGER.info("Executing 'approve' by checker");
        String id = req.getId();
        String url = req.getUrl();
        String methodType = req.getMethod();
        Optional<DataObject> co = dor.findById(id);
        DataObject currentObject;
        if(co.isPresent())
            currentObject = co.get();
        else
            throw new ResourceNotFoundException("Object cannot be found.");

        if (currentObject.getStatus() != Status.PENDING)
            throw new AlreadyExistsException("Object Already checked");

        currentObject.setStatus(Status.APPROVED);
        DataObject savedObject = currentObject;

        Object payload = savedObject.getData();

        HttpMethod httpMethod = switch (methodType.toUpperCase()) {
            case "POST" -> HttpMethod.POST;
            case "PUT" -> HttpMethod.PUT;
            default ->
                    throw new IllegalArgumentException("Invalid HTTP method type: " + methodType);
        };

        String stringPayload = null;
        try {
            stringPayload = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }
        HttpEntity<String> requestEntity = new HttpEntity<>(stringPayload, headers);

        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                    url,
                    httpMethod,
                    requestEntity,
                    String.class
            );

            // Handle the case where the response status code is 400
            if (responseEntity.getStatusCode() != HttpStatus.BAD_REQUEST) {
                savedObject.setStatus(Status.APPROVED);
            }
            savedObject.validateBeforeSave();
            DataObject returnedObject =dor.save(savedObject);
            LOGGER.info("Executed 'approve' by checker");
            return returnedObject;

        } catch (HttpClientErrorException e) {
            // Handle the case where the response status code is 400
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                savedObject.setStatus(Status.REJECTED);
                savedObject.setRejectReason(e.getResponseBodyAsString());
            }
            savedObject.validateBeforeSave();
            DataObject returnedObject = dor.save(savedObject);
            LOGGER.info("Error occurred while executing 'approve' by checker");
            return returnedObject;
        }
    }

    @Override
    public DataObject rejectObject(RejectDTO req) {
        LOGGER.info("Executing 'reject' by checker");
        String id = req.getId();
        String rejectReason = req.getRejectReason();
        Optional<DataObject> co = Optional.of(dor.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Object Not Found")));
        DataObject currentObject = co.get();
        if (currentObject.getStatus() != Status.PENDING)
            throw new AlreadyExistsException("Object Already checked");
        currentObject.setStatus(Status.REJECTED);
        currentObject.setRejectReason(rejectReason);
        currentObject.validateBeforeSave();
        DataObject savedObject = dor.save(currentObject);
        LOGGER.info("Executed 'reject' by checker");
        return savedObject;
    }

    @Override
    public DataObject addObject(DataObjectDTO req) {
        LOGGER.info("Executing 'add' by maker");
        req.setObjectType(req.getObjectType().toUpperCase());
        req.setId(req.getUniqueName() + req.getObjectType());
        DataObject currentObject = DataObject.builder()
                .data(req.getData())
                .username(req.getUsername())
                .userEmail(req.getUserEmail())
                .objectType(ObjectType.valueOf(req.getObjectType()))
                .status(Status.PENDING)
                .id(req.getId())
                .createdDate(req.getCreatedDate())
                .uniqueName(req.getUniqueName())
                .build();
        Optional<DataObject> chk = dor.findById(req.getId());
        if (chk.isPresent()) {
            throw new AlreadyExistsException("Object Already exists");
        }
        currentObject.validateBeforeSave();
        DataObject savedObject = dor.save(currentObject);
        LOGGER.info("Executed 'add' by maker");
        return savedObject;
    }
}
