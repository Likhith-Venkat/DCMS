package com.example.DCMS.repository;

import com.example.DCMS.model.BinRangeReq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinRangeReqRepository extends MongoRepository<BinRangeReq, String> {
    List<BinRangeReq> getByStatus(String status);
}
