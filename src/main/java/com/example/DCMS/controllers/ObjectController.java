package com.example.DCMS.controllers;

import com.example.DCMS.DTOs.ApproveDTO;
import com.example.DCMS.DTOs.DataObjectDTO;
import com.example.DCMS.DTOs.RejectDTO;
import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.enums.Status;
import com.example.DCMS.models.DataObject;
import com.example.DCMS.repositories.DataObjectRepo;
import com.example.DCMS.services.ObjectService;
import com.example.DCMS.services.ObjectServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
@Slf4j
@RestController
@RequestMapping("/mc")
public class ObjectController {

    @Autowired
    private ObjectServiceImpl objectService;


    @Autowired
    private DataObjectRepo  dor;




    @PutMapping(path = "/approve")
    public ResponseEntity<DataObject> approve(@RequestBody ApproveDTO req, HttpServletRequest servletRequest) {

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
        DataObject returnedObject =  objectService.approveObject(req, headers);

        if(returnedObject.getStatus() == Status.REJECTED)
            return new ResponseEntity<>(returnedObject, HttpStatus.BAD_REQUEST);
        return  new ResponseEntity<>(returnedObject, HttpStatus.OK);
    }

    @PutMapping(path = "/rejectobj")
    public ResponseEntity<DataObject> rejectobj(@RequestBody RejectDTO req) {
        DataObject savedObject = objectService.rejectObject(req);
        return new ResponseEntity<>(savedObject, HttpStatus.OK);
    }

    @PostMapping(path = "/addobj")
    public ResponseEntity<DataObject> addobj(@RequestBody DataObjectDTO req) {
        DataObject savedObject = objectService.addObject(req);
        return new ResponseEntity<>(savedObject, HttpStatus.CREATED);
    }

    @GetMapping(path = "/get/{status}/{objectType}")
<<<<<<< HEAD
    public ResponseEntity<List<DataObject>> get(@PathVariable Status status, @PathVariable ObjectType objectType) {
        LOGGER.info("Executing 'get'");
        List<DataObject> dolist = dor.findByStatusAndObjectType(status, objectType);
        LOGGER.info("Executed 'get'");
=======
    public ResponseEntity<List<dataObject>> get(@PathVariable Status status, @PathVariable ObjectType objectType) {
        log.info("Executing 'get'");
        List<dataObject> dolist = dor.findByStatusAndObjectType(status, objectType);
        log  .info("Executed 'get'");
>>>>>>> 3e8dd26fcef0edef8f2748fc549dd04d8f6554a0
        return new ResponseEntity<>(dolist, HttpStatus.OK);
    }
}
