package com.example.DCMS.repositories;

import com.example.DCMS.models.dataObject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface dataObjectRepo extends MongoRepository<dataObject, String> {
}
