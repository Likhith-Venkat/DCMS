package com.example.DCMS.service;

import com.example.DCMS.model.BinRange;
import com.example.DCMS.model.BinRangeReq;
import com.example.DCMS.repository.BinRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinRangeService {
    @Autowired
    BinRangeRepository binRangeRepository;

    public List<BinRange> getAll(){
        return binRangeRepository.findAll();
    }
    BinRange addBin(BinRange binRange){
        return binRangeRepository.save(binRange);
    }
}
