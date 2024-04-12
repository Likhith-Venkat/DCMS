package com.example.DCMS.controllers;

import com.example.DCMS.exception.AlreadyExistsException;
import com.example.DCMS.exception.ResourceNotFoundException;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/mc")
public class ObjectController
{

    @Autowired
    dataObjectRepo dor;
    private static final Logger LOGGER = Logger.getLogger("ObjectController.class");
    @GetMapping(path = "/test")
    public ResponseEntity<?> helloWorld()
    {
        LOGGER.info("test");
        JSONObject test = new JSONObject();
        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }


    @PutMapping(path = "/approveobj")
    public ResponseEntity<?> approveobj(@RequestBody String req)
    {

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
    }
    @PutMapping(path = "/rejectobj")
    public ResponseEntity<?> rejectobj(@RequestBody String req)
    {
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
    }
    @PostMapping(path = "/addobj")
    public ResponseEntity<dataObject> addobj(@RequestBody dataObject req)
    {
            LOGGER.info("Executing 'add' by maker");
            req.setStatus("PENDING");
            req.setObjectType(req.getObjectType().toUpperCase());
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