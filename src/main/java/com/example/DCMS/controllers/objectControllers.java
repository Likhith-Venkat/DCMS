package com.example.DCMS.controllers;

import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/mc")
public class objectControllers
{

    @Autowired
    dataObjectRepo dor;

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
            JSONObject jsonReq = new JSONObject(req);
            String id =jsonReq.getString("id");
            Optional<dataObject> co = dor.findById(id);
            dataObject currentObject;
            if(co.isPresent())
                currentObject = co.get();
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object id does not match with any row");
            if(!Objects.equals(currentObject.getStatus(), "PENDING"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object Already checked");

            currentObject.setStatus("APPROVED");
            dataObject savedObject = dor.save(currentObject);
//            SEND TO DCMS BACKEND
            return new ResponseEntity<>(savedObject, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
        }
    }
    @PutMapping(path = "/rejectobj")
    public ResponseEntity<?> rejectobj(@RequestBody String req)
    {
        try {

            JSONObject jsonReq = new JSONObject(req);
            String id =jsonReq.getString("id");
            String rejectReason = jsonReq.getString("rejectReason");
            Optional<dataObject> co = dor.findById(id);
            dataObject currentObject;
            if(co.isPresent())
                currentObject = co.get();
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object id does not match with any row");
            if(!Objects.equals(currentObject.getStatus(), "PENDING"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object Already checked");
            currentObject.setStatus("REJECTED");
            currentObject.setRejectReason(rejectReason);
            dataObject savedObject = dor.save(currentObject);
            return new ResponseEntity<>(savedObject, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
        }
    }
    @PostMapping(path = "/addobj")
    public ResponseEntity<dataObject> addobj(@RequestBody dataObject req)
    {

        try {
            req.setStatus("PENDING");
            req.setObjectType(req.getObjectType().toUpperCase());
            dataObject savedObject = dor.save(req);
            return new ResponseEntity<>(savedObject, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
        }
    }
    @GetMapping(path = "/get")
    public ResponseEntity<List<dataObject>> get(@RequestBody String request){
        try{
            JSONObject jsonObject =  new JSONObject(request);
            String objectType = jsonObject.getString("objectType");
            String status = jsonObject.getString("status");
            List<dataObject> dolist= dor.findByStatusAndObjectType(status, objectType);
            return new ResponseEntity<>(dolist, HttpStatus.OK);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


}