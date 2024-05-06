package com.example.DCMS.services;

import com.example.DCMS.DTOs.ApproveDTO;
import com.example.DCMS.DTOs.DataObjectDTO;
import com.example.DCMS.DTOs.RejectDTO;
import com.example.DCMS.models.DataObject;
import org.springframework.http.HttpHeaders;

public interface ObjectService {
    DataObject approveObject(ApproveDTO req, HttpHeaders headers);

    DataObject rejectObject(RejectDTO req);

    DataObject addObject(DataObjectDTO req);
}
