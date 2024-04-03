package com.example.DCMS.model;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Bin_req")
public class BinReq {
    @Id
    private String Bin;
    private Date createdDate;
    private User Maker;
    private User Checker;
    private String RejectReason;
    private String Status;
}
