package com.example.DCMS.ObjectTranslation;

import com.example.DCMS.DTO.DataObjectDTO;
import com.example.DCMS.model.DataObject;

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
