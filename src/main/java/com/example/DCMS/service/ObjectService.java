package com.example.DCMS.service;

import com.example.DCMS.DTO.ApproveDTO;
import com.example.DCMS.DTO.ApproveResponseDTO;
import com.example.DCMS.DTO.DataObjectDTO;
import com.example.DCMS.DTO.RejectDTO;
import com.example.DCMS.model.DataObject;
import org.springframework.http.HttpHeaders;

public interface ObjectService {
    ApproveResponseDTO approveObject(ApproveDTO req, HttpHeaders headers);

    DataObject rejectObject(RejectDTO req);

    DataObject addObject(DataObjectDTO req);
}
