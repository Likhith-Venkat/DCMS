package com.example.DCMS.services;

import com.example.DCMS.DTOs.ApproveDTO;
import com.example.DCMS.DTOs.DataObjectDTO;
import com.example.DCMS.DTOs.RejectDTO;
import com.example.DCMS.enums.Method;
import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.enums.Status;
import com.example.DCMS.exception.AlreadyExistsException;
import com.example.DCMS.exception.ResourceNotFoundException;
import com.example.DCMS.models.DataObject;
import com.example.DCMS.repositories.DataObjectRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;

@Slf4j
@Service
public class ObjectServiceImpl implements ObjectService {

    @Autowired
    private DataObjectRepo dataObjectRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper mapper;

    RestTemplate restTemplate = new RestTemplate();


    @Override
    public DataObject approveObject(ApproveDTO req, HttpHeaders headers) {
        log.info("Executing 'approve' by checker");
        String id = req.getId();
        String url = req.getUrl();
        Method method = req.getMethod();
        Optional<DataObject> dataObjectForID = dataObjectRepo.findById(id);
        DataObject currentObject;


        if(dataObjectForID.isPresent())
            currentObject = dataObjectForID.get();
        else
            throw new ResourceNotFoundException("Object cannot be found.");

        if (currentObject.getStatus() != Status.PENDING)
            throw new AlreadyExistsException("Object Already checked");

        currentObject.setStatus(Status.APPROVED);
        DataObject savedObject = currentObject;

        Object payload = savedObject.getData();

        HttpMethod httpMethod = switch (method) {
            case POST -> HttpMethod.POST;
            case PUT -> HttpMethod.PUT;
            default ->
                    throw new IllegalArgumentException("Invalid HTTP method type: " + method);
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

            DataObject returnedObject =dataObjectRepo.save(savedObject);
            log.info("Executed 'approve' by checker");
            return returnedObject;

        } catch (HttpClientErrorException e) {
            // Handle the case where the response status code is 400
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                savedObject.setStatus(Status.REJECTED);
                savedObject.setRejectReason(e.getResponseBodyAsString());
            }

            DataObject returnedObject = dataObjectRepo.save(savedObject);
            log.info("Error occurred while executing 'approve' by checker");
            return returnedObject;
        }
        catch (HttpServerErrorException e) {
            log.error("HTTP Server Error: {}", e.getRawStatusCode());
            throw e;
        }
    }

    @Override
    public DataObject rejectObject(RejectDTO req) {
        log.info("Executing 'reject' by checker");
        String id = req.getId();
        String rejectReason = req.getRejectReason();
        Optional<DataObject> dataObjectForID = Optional.of(dataObjectRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Object Not Found")));
        DataObject currentObject = dataObjectForID.get();
        if (currentObject.getStatus() != Status.PENDING)
            throw new AlreadyExistsException("Object Already checked");
        currentObject.setStatus(Status.REJECTED);
        currentObject.setRejectReason(rejectReason);

        DataObject savedObject = dataObjectRepo.save(currentObject);
        log.info("Executed 'reject' by checker");
        return savedObject;
    }

    @Override
    public DataObject addObject(DataObjectDTO req) {
        log.info("Executing 'add' by maker");
        req.setId(req.getUniqueName() + req.getObjectType());
        DataObject currentObject = mapper.map(req, DataObject.class);
        Optional<DataObject> dataObjectForID = dataObjectRepo.findById(req.getId());
        if (dataObjectForID.isPresent()) {
            throw new AlreadyExistsException("Object Already exists");
        }

        DataObject savedObject = dataObjectRepo.save(currentObject);
        log.info("Executed 'add' by maker");
        return savedObject;
    }
}
