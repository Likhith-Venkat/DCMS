package com.example.DCMS.services;

import com.example.DCMS.DTOs.approveDTO;
import com.example.DCMS.DTOs.dataObjectDTO;
import com.example.DCMS.DTOs.rejectDTO;
import com.example.DCMS.models.dataObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ObjectService {
    dataObject approveObject(approveDTO req, HttpHeaders headers);

    dataObject rejectObject(rejectDTO req);

    dataObject addObject(dataObjectDTO req);
}
