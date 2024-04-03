package com.example.DCMS.repository;

import com.example.DCMS.model.BinReq;
import com.example.DCMS.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
