package com.example.DCMS.controller;

import com.example.DCMS.config.MyUserDetails;
import com.example.DCMS.repository.BinRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BinRangeController
{
    @Autowired
    BinRangeRepository brr;

    @PreAuthorize("hasAuthority('BIN')")
    @GetMapping(path = "/sayhi")
    public String sayhi(Authentication auth)
    {
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return userDetails.getPassword()+userDetails.getAuthorities()+userDetails.toString();

    }

}
