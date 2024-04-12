package com.example.DCMS.models;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "maker_checker_requests")
public class dataObject {
    @Id
    private String id;
    private String method;
    private String uri;
    private String requestBody;
    private Map<String, String> requestHeaders;
    private String userEmail;
    private String username;
    private Object data;
    private Date createdDate;
    private String status;
    private String objectType;
    private String rejectReason;
}