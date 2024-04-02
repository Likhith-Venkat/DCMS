package com.example.DCMS.repository;


import com.example.DCMS.model.BinRange;

import com.example.DCMS.model.Bin;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinRangeRepository extends MongoRepository<BinRange, String>
{

}
