package com.example.DCMS.controller;

import com.example.DCMS.config.MyUserDetails;
import com.example.DCMS.model.*;
import com.example.DCMS.service.*;
import com.example.DCMS.repository.BinRepository;
import com.example.DCMS.repository.BinReqRepository;
import com.example.DCMS.repository.UserRepository;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAuthority('BIN')")
public class binController {
    @Autowired
    BinReqRepository brr;

    @Autowired
    BinRepository br;

    @Autowired
    UserRepository ur;

    @GetMapping(path = "mc/getallbins")
    public String getAllBins(){
       return "bins";
    }

//   @PostMapping(path = "mc/addbin")
//   @PreAuthorize("hasRole('ADMIN') and hasRole('MAKER')")
//    public ResponseEntity<BinReq> addBinReq(Authentication auth, @RequestBody String bin){
//        BinReq binReq = new BinReq();
//        binReq.setBin(bin);
//        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//        Optional<User> currentUser =  ur.findById(userDetails.getUsername());
//        binReq.setMaker(currentUser);
//        binReq.setCreatedDate(new Date());
//
//        BinReq savedReq = brr.save(binReq);
//
//        return new ResponseEntity<>(savedReq, HttpStatus.CREATED);
//    }
//

    @PostMapping(path = "/approvebin")
    public ResponseEntity<Bin> approveBin(Authentication auth, @RequestBody Bin bin)
    {
        Bin bincr = br.save(bin);
        return new ResponseEntity<>(bincr, HttpStatus.CREATED);

    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: You do not have permission to access this resource.");
    }
}
