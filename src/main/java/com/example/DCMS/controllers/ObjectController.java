package com.example.DCMS.controllers;

import com.example.DCMS.DTOs.approveDTO;
import com.example.DCMS.DTOs.dataObjectDTO;
import com.example.DCMS.DTOs.rejectDTO;
import com.example.DCMS.enums.Status;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import com.example.DCMS.services.ObjectServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/mc")
public class ObjectController {

    @Autowired
    private ObjectServiceImpl objectService;


    @Autowired
    private dataObjectRepo  dor;

    private static final Logger LOGGER = Logger.getLogger("ObjectController.class");


    @PutMapping(path = "/approve")
    public ResponseEntity<dataObject> approve(@RequestBody approveDTO req, HttpServletRequest servletRequest) {
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
        dataObject returnedObject =  objectService.approveObject(req, headers);

        if(returnedObject.getStatus() == Status.REJECTED)
            return new ResponseEntity<>(returnedObject, HttpStatus.BAD_REQUEST);
        return  new ResponseEntity<>(returnedObject, HttpStatus.OK);
    }

    @PutMapping(path = "/rejectobj")
    public ResponseEntity<dataObject> rejectobj(@RequestBody rejectDTO req) {
        dataObject savedObject = objectService.rejectObject(req);
        return new ResponseEntity<>(savedObject, HttpStatus.OK);
    }

    @PostMapping(path = "/addobj")
    public ResponseEntity<dataObject> addobj(@RequestBody @Valid dataObjectDTO req) {
        dataObject savedObject = objectService.addObject(req);
        return new ResponseEntity<>(savedObject, HttpStatus.CREATED);
    }

    @GetMapping(path = "/get/{status}/{objectType}")
    public ResponseEntity<List<dataObject>> get(@PathVariable String status, @PathVariable String objectType) {
        LOGGER.info("Executing 'get'");
        List<dataObject> dolist = dor.findByStatusAndObjectType(status, objectType);
        LOGGER.info("Executed 'get'");
        return new ResponseEntity<>(dolist, HttpStatus.OK);
    }
}
