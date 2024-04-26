package com.example.DCMS.DTOs;


import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
