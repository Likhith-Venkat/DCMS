package com.example.DCMS.models;

import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.exception.DocumentValidationException;
import lombok.*;

import com.example.DCMS.enums.Status;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "maker_checker_requests")
public class DataObject {
    @Id
    private String id;
    private String userEmail;
    private String username;
    private Object data;
    @CreatedDate
    private Date createdDate;
    private Status status;
    private ObjectType objectType;
    private String rejectReason;
    private String uniqueName;
}