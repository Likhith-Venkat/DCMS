package com.example.DCMS.models;

import com.example.DCMS.exception.documentValidationException;
import lombok.*;

import org.json.JSONObject;
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
public class dataObject {
    @Id
    private String id;
    private String userEmail;
    private String username;
    private Object data;
    private Date createdDate;
    private String status;
    private String objectType;
    private String rejectReason;
    public void validateBeforeSave() {
        Map<String, String> mp = new HashMap<>();
        if(data == null)
            mp.put("data", "data cannot be null");
        if(objectType == null)
            mp.put("objectType", "objectType cannot be null");
        if (data == null||objectType == null) {
            throw new documentValidationException(mp);
        }
    }
}