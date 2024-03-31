package com.example.DCMS.model;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Getter
@Setter
@Document(collection="Bin_req")
public class BinReq {
    @Id
    private String Bin;
    private Date created_date;
    private User Maker;
    private User Checker;
    private String Reject_reason;
    private String Status;
}
