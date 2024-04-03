package com.example.DCMS.controller;

import com.example.DCMS.config.MyUserDetails;
import com.example.DCMS.model.*;
import com.example.DCMS.repository.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;
import org.springframework.web.server.ResponseStatusException;


import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('BINRANGE')")
@RequestMapping("/mc")
public class objectsController
{


    @Autowired
    MongoOperations mongoOperations;



    //    @PreAuthorize("hasRole('CHECKER')")
//    @PostMapping(path = "/approvebinrange")
//    public ResponseEntity<BinRange> approveBinRange(Authentication auth, @RequestBody BinRangeReq brreq)
//    {
//        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//        if (Objects.equals(brreq.getStatus(), "PENDING"))
//        {
//            brreq.setStatus("ACCEPTED");
//            Optional<User> ckr = ur.findById(userDetails.getUsername());
//            if(ckr.isPresent())
//            {
//                brreq.setChecker(ckr.get());
//            }
//            else
//            {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//            BinRangeReq brreq1 = brrr.save(brreq);
//            BinRange br = new BinRange(brreq1.getBin(), brreq1.getBin_Range_Name(), brreq1.getProduct_Code(), brreq1.getFrom_Card_Number(), brreq1.getTo_Card_Number(), brreq1.getNetwork_Type());
//            BinRange brr1 = brr.save(br);
//            return new ResponseEntity<>(brr1, HttpStatus.CREATED);
//        }
//        else
//        {
//            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
//        }
//
//    }
//    @PreAuthorize("hasRole('CHECKER')")
//    @PostMapping(path = "/rejectbinrange")
//    public ResponseEntity<BinRangeReq> rejectBinRange(Authentication auth,  @RequestBody BinRangeReqDTO req)
//    {
//        System.out.println("HELLO I am in");
//        BinRangeReq brreq = req.getBrreq();
//        System.out.println(brreq);
//        String rejectReason = req.getRejectReason();
//        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//        if (Objects.equals(brreq.getStatus(), "PENDING"))
//        {
//            brreq.setStatus("REJECTED");
//            Optional<User> ckr = ur.findById(userDetails.getUsername());
//            if(ckr.isPresent())
//            {
//                brreq.setChecker(ckr.get());
//            }
//            else
//            {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//            brreq.setReject_Reason(rejectReason);
//            BinRangeReq brreq1 = brrr.save(brreq);
//
//            return new ResponseEntity<>(brreq1, HttpStatus.OK);
//        }
//        else
//        {
//            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
//        }
//
//    }
    @PreAuthorize("hasRole('ROLE_MAKER')")
    @PostMapping(path = "/hello")
    public String hello(Authentication auth)
    {
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return "hello "+ userDetails.getUsername();

    }
    @PreAuthorize("hasRole('ROLE_MAKER')")
    @PostMapping(path = "/addbinrangereq")
    public ResponseEntity<JSONObject> addBinRangeReq(Authentication auth, @RequestBody String req)
    {
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        try {
            JSONObject jsonReq = new JSONObject(req);
            jsonReq.put("status", "PENDING");
            jsonReq.put("maker", userDetails.getUsername());
            Document document = Document.parse(jsonReq.toString());
            mongoOperations.save(document, "mcobjects");
            return ResponseEntity.ok(jsonReq);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
        }
//        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//        req.setMaker(userDetails.getUsername());
//        req.setStatus("PENDING");

//        if(bin.isPresent())
//        {
//            bin1 = bin.get();
//        }
//        else
//        {
//            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//        }
//        Optional<User> usr1 =  ur.findById(userDetails.getUsername());
//        User usr;
//        if(usr1.isPresent())
//        {
//            usr = usr1.get();
//        }
//        else
//        {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        BinRangeReq brreq = new BinRangeReq(bin1, Bin_Range_Name, Product_Code, From_Card_Number, To_Card_Number, Network_Type, usr, null, null, "PENDING");
//        BinRangeReq savedbrr = brrr.save(brreq);
//        return new ResponseEntity<>(savedbrr, HttpStatus.CREATED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: You do not have permission to access this resource.");
    }


}
