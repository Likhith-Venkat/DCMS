package com.example.DCMS.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class dataObjectDTO {
    private String id;
    private String userEmail;
    private String username;
    private Object data;
    private Date createdDate;
    private String status;
    private String objectType;
    private String rejectReason;
    private String uniqueName;
}
