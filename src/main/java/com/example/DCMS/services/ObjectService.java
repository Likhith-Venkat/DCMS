package com.example.DCMS.services;

import com.example.DCMS.DTOs.ApproveDTO;
import com.example.DCMS.DTOs.DataObjectDTO;
import com.example.DCMS.DTOs.RejectDTO;
import com.example.DCMS.models.DataObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ObjectService {
    DataObject approveObject(ApproveDTO req, HttpHeaders headers);

    DataObject rejectObject(RejectDTO req);

    DataObject addObject(DataObjectDTO req);
}
