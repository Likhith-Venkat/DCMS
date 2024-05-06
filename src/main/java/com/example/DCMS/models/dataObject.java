package com.example.DCMS.models;

import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.exception.documentValidationException;
import lombok.*;

import com.example.DCMS.enums.Status;
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
    private Status status;
    private ObjectType objectType;
    private String rejectReason;
    private String uniqueName;
    public void validateBeforeSave() {
        Map<String, String> mp = new HashMap<>();
        if(data == null)
            mp.put("data", "data cannot be null");
        if(objectType == null)
            mp.put("objectType", "objectType cannot be null");
        if(uniqueName == null)
            mp.put("uniqueName", "uniqueName cannot be null");
        if (data == null||objectType == null|| uniqueName == null) {
            throw new documentValidationException(mp);
        }
    }
}