package com.example.DCMS.ObjectTranslation;

import com.example.DCMS.DTOs.DataObjectDTO;
import com.example.DCMS.models.DataObject;
import org.springframework.context.annotation.Bean;

public class ObjectServiceTranslation
{
    public DataObject DataObjectDTOtoModel(DataObjectDTO dto)
    {
        return DataObject.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .userEmail(dto.getUserEmail())
                .data(dto.getData())
                .createdDate(dto.getCreatedDate())
                .status(dto.getStatus())
                .objectType(dto.getObjectType())
                .rejectReason(dto.getRejectReason())
                .uniqueName(dto.getUniqueName())
                .build();
    }
}
