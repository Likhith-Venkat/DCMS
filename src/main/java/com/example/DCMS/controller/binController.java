package com.example.DCMS.controller;

import com.example.DCMS.model.Bin;
import com.example.DCMS.repository.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class binController {
    @Autowired
    BinRepository br;

    @GetMapping(path = "mc/getallbins")
    @Secured("ADMIN")
    public String getAllBins(){
        return "bins";
    }

    @PostMapping(path = "mc/addbin")
    @PreAuthorize("hasRole('ADMIN') and hasRole('MAKER')")
    public ResponseEntity<Bin> addBin(@RequestBody Bin bin){
        Bin newBin = br.save(bin);
        return new ResponseEntity<>(newBin, HttpStatus.CREATED);
    }
}
