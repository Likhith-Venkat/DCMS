package com.example.DCMS.controller;

import com.example.DCMS.model.BinRangeReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BinRangeReqDTO
{
    private BinRangeReq brreq;
    private String rejectReason;

}
