package com.example.DCMS.DTOs;

import com.example.DCMS.enums.Status;
import com.example.DCMS.models.DataObject;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveResponseDTO
{
    private HttpStatus httpStatus;
    private Status dataObjectStatus;
    private String rejectReason;
    private Object data;
    private DataObject dataObject;
}
