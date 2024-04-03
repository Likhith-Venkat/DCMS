package com.example.DCMS.model;
import java.util.Date;

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
    private String bin;
    private Date createdDate;
    private String maker;
    private String checker;
    private String rejectReason;
    private String status;
}
