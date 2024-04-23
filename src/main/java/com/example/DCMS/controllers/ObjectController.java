package com.example.DCMS.controllers;

import com.example.DCMS.DTOs.approveDTO;
import com.example.DCMS.DTOs.dataObjectDTO;
import com.example.DCMS.DTOs.rejectDTO;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import com.example.DCMS.services.ObjectServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/mc")
public class ObjectController {

    @Autowired
    private ObjectServiceImpl objectService;

    @Autowired
    private dataObjectRepo dor;

    private static final Logger LOGGER = Logger.getLogger("ObjectController.class");

    @GetMapping(path = "/test")
    public ResponseEntity<String> helloWorld() {
        LOGGER.info("test");
        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }

    @PutMapping(path = "/approve")
    public ResponseEntity<String> approve(@RequestBody approveDTO req, HttpServletRequest servletRequest) {
        return objectService.approveObject(req, servletRequest);
    }

    @PutMapping(path = "/rejectobj")
    public ResponseEntity<dataObject> rejectobj(@RequestBody rejectDTO req) {
        return objectService.rejectObject(req);
    }

    @PostMapping(path = "/addobj")
    public ResponseEntity<dataObject> addobj(@RequestBody dataObjectDTO req) {
        return objectService.addObject(req);
    }

    @GetMapping(path = "/get/{status}/{objectType}")
    public ResponseEntity<List<dataObject>> get(@PathVariable String status, @PathVariable String objectType) {
        LOGGER.info("Executing 'get'");
        List<dataObject> dolist = dor.findByStatusAndObjectType(status, objectType);
        LOGGER.info("Executed 'get'");
        return new ResponseEntity<>(dolist, HttpStatus.OK);
    }
}
