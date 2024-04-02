package com.example.DCMS.controller;

import com.example.DCMS.config.MyUserDetails;
import com.example.DCMS.model.Bin;
import com.example.DCMS.model.BinRange;
import com.example.DCMS.model.BinRangeReq;
import com.example.DCMS.model.User;
import com.example.DCMS.repository.BinRangeRepository;
import com.example.DCMS.repository.BinRangeReqRepository;
import com.example.DCMS.repository.BinRepository;
import com.example.DCMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('BINRANGE')")
public class BinRangeController
{
    @Autowired
    BinRangeRepository brr;
    @Autowired
    BinRangeReqRepository brrr;

    @Autowired
    UserRepository ur;

    @Autowired
    BinRepository breq;

    @PreAuthorize("hasRole('CHECKER')")
    @PostMapping(path = "/approvebinrange")
    public ResponseEntity<BinRange> approveBinRange(Authentication auth, @RequestBody BinRangeReq brreq)
    {
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        if (Objects.equals(brreq.getStatus(), "PENDING"))
        {
            brreq.setStatus("ACCEPTED");
            Optional<User> ckr = ur.findById(userDetails.getUsername());
            if(ckr.isPresent())
            {
                brreq.setChecker(ckr.get());
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            BinRangeReq brreq1 = brrr.save(brreq);
            BinRange br = new BinRange(brreq1.getBin(), brreq1.getBin_Range_Name(), brreq1.getProduct_Code(), brreq1.getFrom_Card_Number(), brreq1.getTo_Card_Number(), brreq1.getNetwork_Type());
            BinRange brr1 = brr.save(br);
            return new ResponseEntity<>(brr1, HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

    }
    @PreAuthorize("hasRole('CHECKER')")
    @PostMapping(path = "/rejectbinrange")
    public ResponseEntity<BinRangeReq> rejectBinRange(Authentication auth, @RequestBody BinRangeReq brreq, @RequestBody String rejectReason)
    {
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        if (Objects.equals(brreq.getStatus(), "PENDING"))
        {
            brreq.setStatus("REJECTED");
            Optional<User> ckr = ur.findById(userDetails.getUsername());
            if(ckr.isPresent())
            {
                brreq.setChecker(ckr.get());
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            brreq.setReject_Reason(rejectReason);
            BinRangeReq brreq1 = brrr.save(brreq);

            return new ResponseEntity<>(brreq1, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

    }
    @PreAuthorize("hasRole('ROLE_MAKER')")
    @PostMapping(path = "/hello")
    public String hello(Authentication auth)
    {
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return "hello "+ userDetails.getUsername();

    }
    @PreAuthorize("hasRole('ROLE_MAKER')")
    @PostMapping(path = "/addbinrangereq")
    public ResponseEntity<BinRangeReq> addBinRangeReq(Authentication auth, @RequestBody String Binno,  @RequestBody String Bin_Range_Name, @RequestBody Integer Product_Code, @RequestBody Integer From_Card_Number, @RequestBody Integer To_Card_Number, @RequestBody String Network_Type)
    {
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<Bin> bin = breq.findById(Binno);
        Bin bin1;
        System.out.println("HELLO I am in");
        if(bin.isPresent())
        {
            bin1 = bin.get();
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<User> usr1 =  ur.findById(userDetails.getUsername());
        User usr;
        if(usr1.isPresent())
        {
            usr = usr1.get();
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BinRangeReq brreq = new BinRangeReq(bin1, Bin_Range_Name, Product_Code, From_Card_Number, To_Card_Number, Network_Type, usr, null, null, "PENDING");
        BinRangeReq savedbrr = brrr.save(brreq);
        return new ResponseEntity<>(savedbrr, HttpStatus.CREATED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: You do not have permission to access this resource.");
    }

}
