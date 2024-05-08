package com.example.DCMS.repository;

import com.example.DCMS.enums.*;
import com.example.DCMS.model.DataObject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DataObjectRepo extends MongoRepository<DataObject, String> {
    List<DataObject> findByStatusAndObjectType(Status status, ObjectType objectType);
}
