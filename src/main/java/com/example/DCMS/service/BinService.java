package com.example.DCMS.service;

import com.example.DCMS.model.Bin;
import com.example.DCMS.model.BinReq;
import com.example.DCMS.repository.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinService {
    @Autowired
    BinRepository binRepository;

    public List<Bin> getAll(){
        return binRepository.findAll();
    }
    Bin addBin(Bin bin){
        return binRepository.save(bin);
    }
}
