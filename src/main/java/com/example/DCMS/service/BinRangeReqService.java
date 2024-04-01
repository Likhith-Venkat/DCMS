package com.example.DCMS.service;

import com.example.DCMS.model.BinRangeReq;
import com.example.DCMS.repository.BinRangeReqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinRangeReqService {
    @Autowired
    BinRangeReqRepository binRangeReqRepository;
    public List<BinRangeReq> getAll(){
        return binRangeReqRepository.findAll();
    }

    List<BinRangeReq> getPending(){
        return binRangeReqRepository.getByStatus("PENDING");
    }
    List<BinRangeReq> getAccepted(){
        return binRangeReqRepository.getByStatus("ACCEPTED");
    }
    List<BinRangeReq> getRejected(){
        return binRangeReqRepository.getByStatus("REJECTED");
    }
    BinRangeReq addBinReq(BinRangeReq binRange){
        return binRangeReqRepository.save(binRange);
    }
}
