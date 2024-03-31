package com.example.DCMS.repository;

import com.example.DCMS.model.Bin;
import com.example.DCMS.model.BinReq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinReqRepository extends MongoRepository<BinReq, String> {
    List<BinReq> getByStatus(String status);
}
