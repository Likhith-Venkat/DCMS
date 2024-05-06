package com.example.DCMS.repositories;

import com.example.DCMS.enums.*;
import com.example.DCMS.models.DataObject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DataObjectRepo extends MongoRepository<DataObject, String> {
    List<DataObject> findByStatusAndObjectType(Status status, ObjectType objectType);
}
