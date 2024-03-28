package com.example.DCMS.service;

import com.example.DCMS.model.Bin;
import com.example.DCMS.model.BinReq;
import com.example.DCMS.repository.BinReqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinReqService {
    @Autowired
    BinReqRepository binReqRepository;

    List<BinReq> getPending(){
        return binReqRepository.getByStatus("PENDING");
    }
    List<BinReq> getAccepted(){
        return binReqRepository.getByStatus("ACCEPTED");
    }
    List<BinReq> getRejected(){
        return binReqRepository.getByStatus("REJECTED");
    }
    Bin addBin(Bin bin){
        return binReqRepository.save(bin);
    }
}
