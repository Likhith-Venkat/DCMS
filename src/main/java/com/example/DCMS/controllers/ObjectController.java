package com.example.DCMS.controllers;

import com.example.DCMS.exception.AlreadyExistsException;
import com.example.DCMS.exception.ResourceNotFoundException;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/mc")
public class ObjectController
{

    @Autowired
    dataObjectRepo dor;
    
    static final String APPROVED = "APPROVED";
    static final String REJECTED = "REJECTED";
    static final String PENDING = "PENDING";
        
    
    @Autowired
    ObjectMapper objectMapper;
    private static final Logger LOGGER = Logger.getLogger("ObjectController.class");
    @GetMapping(path = "/test")
    public ResponseEntity<String> helloWorld()
    {
        LOGGER.info("test");
        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }


    @PutMapping(path = "/approve")
    public ResponseEntity<String> approve(@RequestBody String req) throws JsonProcessingException {

            LOGGER.info("Executing 'approve' by checker");
            JSONObject jsonReq = new JSONObject(req);
            String id =jsonReq.getString("id");
            Optional<dataObject> co = Optional.ofNullable(dor.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Object cannot be found.")));

            dataObject currentObject= co.get();
            if(!Objects.equals(currentObject.getStatus(), PENDING))
                throw new AlreadyExistsException("Object Already checked");

            currentObject.setStatus(APPROVED);
            dataObject savedObject = currentObject;

            String url = savedObject.getUri();
            Object payload = savedObject.getData();
            HttpHeaders headers = new HttpHeaders();
            Map<String, String> headersMap = savedObject.getRequestHeaders();


            headersMap.forEach((key, value) -> headers.add(key, value));

            String stringPayload = objectMapper.writeValueAsString(payload);
            HttpEntity<String> requestEntity = new HttpEntity<>(stringPayload, headers);
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
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
    @PutMapping(path = "/rejectobj")
    public ResponseEntity<dataObject> rejectobj(@RequestBody String req)
    {
            LOGGER.info("Executing 'reject' by checker");
            JSONObject jsonReq = new JSONObject(req);
            String id =jsonReq.getString("id");
            String rejectReason = jsonReq.getString("rejectReason");
            Optional<dataObject> co = Optional.of(dor.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Object Not Found")));
            dataObject currentObject= co.get();
            if(!Objects.equals(currentObject.getStatus(), PENDING))
                throw new AlreadyExistsException("Object Already checked");
            currentObject.setStatus(REJECTED);
            currentObject.setRejectReason(rejectReason);
            currentObject.validateBeforeSave();
            dataObject savedObject = dor.save(currentObject);
            LOGGER.info("Executed 'reject' by checker");
            return new ResponseEntity<>(savedObject, HttpStatus.OK);
    }
    @PostMapping(path = "/addobj")
    public ResponseEntity<dataObject> addobj(@RequestBody dataObject req)
    {
            LOGGER.info("Executing 'add' by maker");
            req.setStatus(PENDING);
            req.setObjectType(req.getObjectType().toUpperCase());
            req.validateBeforeSave();
            dataObject savedObject = dor.save(req);
            LOGGER.info("Executed 'add' by maker");
            return new ResponseEntity<>(savedObject, HttpStatus.CREATED);
    }
    @GetMapping(path = "/get/{status}/{objectType}")
    public ResponseEntity<List<dataObject>> get(@PathVariable String status, @PathVariable String objectType){
            LOGGER.info("Executing 'get'");
            List<dataObject> dolist= dor.findByStatusAndObjectType(status, objectType);
            LOGGER.info("Executed 'get'");
            return new ResponseEntity<>(dolist, HttpStatus.OK);
    }


}