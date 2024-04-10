package com.example.DCMS.repositories;

import com.example.DCMS.models.dataObject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface dataObjectRepo extends MongoRepository<dataObject, String> {
    List<dataObject> findByStatusAndObjectType(String status, String objectType);
}
