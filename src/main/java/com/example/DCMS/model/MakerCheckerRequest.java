package com.example.DCMS.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Setter
@Getter
@Document(collection = "maker_checker_requests")
public class MakerCheckerRequest {
    @Id
    private String id;
    private String method;
    private String uri;
    private String requestBody;
    private Map<String, String> requestHeaders;
    private String maker;
    private String checker;
    private Date madeDate;
    private Date checkedDate;
    private String status;
    private String objectType; 
}

