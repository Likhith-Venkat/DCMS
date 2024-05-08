package com.example.DCMS.controllers;

import com.example.DCMS.DTOs.ApproveDTO;
import com.example.DCMS.DTOs.ApproveResponseDTO;
import com.example.DCMS.DTOs.DataObjectDTO;
import com.example.DCMS.DTOs.RejectDTO;
import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.enums.Status;
import com.example.DCMS.models.DataObject;
import com.example.DCMS.repositories.DataObjectRepo;
import com.example.DCMS.services.ObjectServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mc")
public class ObjectController {

    @Autowired
    private ObjectServiceImpl objectService;


    @Autowired
    private DataObjectRepo  dataObjectRepo;

    @PutMapping(path = "/approve")
    public ResponseEntity<ApproveResponseDTO> approve(@RequestBody ApproveDTO req, HttpServletRequest servletRequest) {

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
        ApproveResponseDTO approvedObject =  objectService.approveObject(req, headers);

        if(approvedObject.getDataObjectStatus() == Status.REJECTED)
            return new ResponseEntity<>(approvedObject, HttpStatus.BAD_REQUEST);
        return  new ResponseEntity<>(approvedObject, HttpStatus.OK);
    }

    @PutMapping(path = "/rejectobj")
    public ResponseEntity<DataObject> rejectobj(@RequestBody RejectDTO req) {
        DataObject rejectedObject = objectService.rejectObject(req);
        return new ResponseEntity<>(rejectedObject, HttpStatus.OK);
    }

    @PostMapping(path = "/addobj")
    public ResponseEntity<DataObject> addobj(@RequestBody @Valid DataObjectDTO req) {
        DataObject savedObject = objectService.addObject(req);
        return new ResponseEntity<>(savedObject, HttpStatus.CREATED);
    }

    @GetMapping(path = "/get/{status}/{objectType}")
    public ResponseEntity<List<DataObject>> get(@PathVariable Status status, @PathVariable ObjectType objectType) {
        log.info("Executing 'get'");
        List<DataObject> dolist = dataObjectRepo.findByStatusAndObjectType(status, objectType);
        log .info("Executed 'get'");
        return new ResponseEntity<>(dolist, HttpStatus.OK);
    }
}
