package com.example.DCMS.services;

import com.example.DCMS.DTOs.approveDTO;
import com.example.DCMS.DTOs.dataObjectDTO;
import com.example.DCMS.DTOs.rejectDTO;
import com.example.DCMS.models.dataObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ObjectServiceInterface {
    ResponseEntity<String> approveObject(approveDTO req, HttpServletRequest servletRequest);

    ResponseEntity<dataObject> rejectObject(rejectDTO req);

    ResponseEntity<dataObject> addObject(dataObjectDTO req);
}
