package com.example.DCMS.controllers;

import com.example.DCMS.exception.AlreadyExistsException;
import com.example.DCMS.exception.ResourceNotFoundException;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@Log4j
@RequestMapping("/mc")
public class ObjectController
{

    @Autowired
    dataObjectRepo dor;
    private static final Logger LOGGER = Logger.getLogger("ObjectController.class");
    @GetMapping(path = "/test")
    public ResponseEntity<?> helloWorld()
    {
        JSONObject test = new JSONObject();
        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }


    @PutMapping(path = "/approveobj")
    public ResponseEntity<?> approveobj(@RequestBody String req)
    {

        try {
            LOGGER.info("Executing 'approve' by checker");
            JSONObject jsonReq = new JSONObject(req);
            String id =jsonReq.getString("id");
            Optional<dataObject> co = Optional.ofNullable(dor.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Object cannot be found.")));

            dataObject currentObject= co.get();
            if(!Objects.equals(currentObject.getStatus(), "PENDING"))
                throw new AlreadyExistsException("Object Already checked");

            currentObject.setStatus("APPROVED");
            dataObject savedObject = dor.save(currentObject);
            LOGGER.info("Executed 'approve' by checker");
            return new ResponseEntity<>(savedObject, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warning("Approval declined: BAD request");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
        }
    }
    @PutMapping(path = "/rejectobj")
    public ResponseEntity<?> rejectobj(@RequestBody String req)
    {
        try {
            LOGGER.info("Executing 'reject' by checker");
            JSONObject jsonReq = new JSONObject(req);
            String id =jsonReq.getString("id");
            String rejectReason = jsonReq.getString("rejectReason");
            Optional<dataObject> co = Optional.of(dor.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Object Not Found")));
            dataObject currentObject= co.get();
            if(!Objects.equals(currentObject.getStatus(), "PENDING"))
                throw new AlreadyExistsException("Object Already checked");
            currentObject.setStatus("REJECTED");
            currentObject.setRejectReason(rejectReason);
            dataObject savedObject = dor.save(currentObject);
            LOGGER.info("Executed 'reject' by checker");
            return new ResponseEntity<>(savedObject, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warning("Rejection declined: BAD request");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
        }
    }
    @PostMapping(path = "/addobj")
    public ResponseEntity<dataObject> addobj(@RequestBody dataObject req)
    {

        try {
            LOGGER.info("Executing 'add' by maker");
            req.setStatus("PENDING");
            req.setObjectType(req.getObjectType().toUpperCase());
            dataObject savedObject = dor.save(req);
            LOGGER.info("Executed 'add' by maker");
            return new ResponseEntity<>(savedObject, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.warning("Add object declined: BAD request");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
        }
    }
    @GetMapping(path = "/get")
    public ResponseEntity<List<dataObject>> get(@RequestBody String request){
        try{
            LOGGER.info("Executing 'get'");
            JSONObject jsonObject =  new JSONObject(request);
            String objectType = jsonObject.getString("objectType");
            String status = jsonObject.getString("status");
            List<dataObject> dolist= dor.findByStatusAndObjectType(status, objectType);
            LOGGER.info("Executed 'get'");
            return new ResponseEntity<>(dolist, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.warning("Get declined: BAD request");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


}