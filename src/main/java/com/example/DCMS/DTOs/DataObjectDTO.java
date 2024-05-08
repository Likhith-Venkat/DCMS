package com.example.DCMS.DTOs;


import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataObjectDTO {
    private String id;
    @Email(message = "Not a valid email id")
    private String userEmail;
    private String username;
    @NotNull(message = "data cannot be null")
    private Object data;
    private Date createdDate;
    private Status status;
    @NotNull(message = "objectType cannot be null")
    private ObjectType objectType;
    private String rejectReason;
    @NotNull(message = "uniqueName cannot be null")
    private String uniqueName;
}
