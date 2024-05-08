package com.example.DCMS.services;

import com.example.DCMS.DTOs.*;
import com.example.DCMS.ObjectTranslation.ObjectServiceTranslation;
import com.example.DCMS.enums.Method;
import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.enums.Status;
import com.example.DCMS.exception.AlreadyExistsException;
import com.example.DCMS.exception.ResourceNotFoundException;
import com.example.DCMS.models.DataObject;
import com.example.DCMS.repositories.DataObjectRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@AutoConfigureDataMongo
@ExtendWith(MockitoExtension.class)
class ObjectServiceImplTest {

    @Mock
    private DataObjectRepo dor;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RestTemplate restTemplate;
    private DataObject currentObject;
    private DataObjectDTO doDTO;
    private RejectDTO rejDTO;
    private ApproveDTO apprDTO;




    private BinDTO dt;
    HttpHeaders headers = new HttpHeaders();
    @InjectMocks
    ObjectServiceImpl objserv;



    @BeforeEach
    void setUp() {
        dt = BinDTO.builder()
                .binValue("764673")
                .checkSIExternal(false)
                .billingCurrency("356")
                .status(true)
                .binType("DEBIT")
                .build();
        doDTO = DataObjectDTO.builder()
                .userEmail("abc@gmail.com")
                .username("abc")
                .data(dt)
                .objectType(ObjectType.BIN)
                .uniqueName("764673")
                .build();
        currentObject = DataObject.builder()
                .id("764673BIN")
                .userEmail("abc@gmail.com")
                .username("abc")
                .data(dt)
                .status(Status.PENDING)
                .objectType(ObjectType.BIN)
                .uniqueName("764673")
                .build();
        rejDTO = RejectDTO.builder()
                .rejectReason("Bad credentials")
                .id("764673BIN")
                .build();
        apprDTO = ApproveDTO.builder()
                .url("https://uat-dcms.m2pfintech.com/dcms-authnt/api/bins")
                .method(Method.POST)
                .id("764673BIN")
                .build();
        headers.add("X-TENANT-ID", "MAHESHBANK");
        headers.add("content-type", "application/json");

    }



    @Test
    void approveObject_returnsOK() throws JsonProcessingException {
        given(dor.save(Mockito.any(DataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(currentObject.getData().toString(), HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(responseEntity);
        ApproveResponseDTO res = objserv.approveObject(apprDTO, headers);
        currentObject.setStatus(Status.APPROVED);
        ApproveResponseDTO approveResponseDTO = new ApproveResponseDTO(HttpStatus.OK, Status.APPROVED, "NOT APPLICABLE", currentObject.getData(), currentObject);
        System.out.println(approveResponseDTO);
        System.out.println(res);
        Assertions.assertThat(res).isEqualTo(approveResponseDTO);
    }


    @Test
    void approveObject_wrongMethod()
    {
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));
        apprDTO.setMethod(Method.DELETE);

        // Define the expected exception
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            // Call the method that should throw the exception
            objserv.approveObject(apprDTO, headers);
        });
    }

    @Test
    void approveObject_DataObjectNotFound()
    {
        when(dor.findById("764673BIN")).thenReturn(Optional.empty());
        // Define the expected exception
        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class, () -> {
            // Call the method that should throw the exception
            objserv.approveObject(apprDTO, headers);
        });
    }

    @Test
    void approveObject_alreadyExistsException()
    {
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));
        currentObject.setStatus(Status.APPROVED);
        // Define the expected exception
        AlreadyExistsException thrownException = assertThrows(AlreadyExistsException.class, () -> {
            // Call the method that should throw the exception
            objserv.approveObject(apprDTO, headers);
        });
    }

//
//    @Test
//    void approveObject_throwsBadRequest()
//    {
//        given(dor.save(Mockito.any(DataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
//        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));
//
//        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Req, Wrong Bin");
//
//        when(restTemplate.exchange(
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(HttpMethod.class),
//                ArgumentMatchers.any(),
//                ArgumentMatchers.<Class<String>>any()))
//                .thenThrow(exception);
//        ApproveResponseDTO res = objserv.approveObject(apprDTO, headers);
//        currentObject.setStatus(Status.REJECTED);
//        currentObject.setRejectReason("Bad Req, Wrong Bin");
//        ApproveResponseDTO approveResponseDTO = new ApproveResponseDTO(HttpStatus.BAD_REQUEST, Status.REJECTED, currentObject.getRejectReason(), currentObject.getData(), currentObject);
//        System.out.println(res.getDataObject());
//        System.out.println(res.getRejectReason());
//        Assertions.assertThat(res).isEqualTo(approveResponseDTO);
//    }

//    @Test
//    void approveObject_returnsBadRequest()
//    {
//        given(dor.save(Mockito.any(DataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
//        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Bad Req, Wrong Bin", HttpStatus.BAD_REQUEST);
//
//        when(restTemplate.exchange(
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(HttpMethod.class),
//                ArgumentMatchers.any(),
//                ArgumentMatchers.<Class<String>>any()))
//                .thenReturn(responseEntity);
//        ApproveResponseDTO res = objserv.approveObject(apprDTO, headers);
//        currentObject.setStatus(Status.REJECTED);
//        currentObject.setRejectReason("Bad Req, Wrong Bin");
//        ApproveResponseDTO approveResponseDTO = new ApproveResponseDTO(HttpStatus.BAD_REQUEST, Status.REJECTED, "Bad Req, Wrong Bin", currentObject.getData(), currentObject);
//        Assertions.assertThat(res).isEqualTo(approveResponseDTO);
//    }
//
    @Test
    void rejectObject()
    {
        given(dor.save(Mockito.any(DataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));
        DataObject savedObject = objserv.rejectObject(rejDTO);
        currentObject.setStatus(Status.REJECTED);
        currentObject.setRejectReason(rejDTO.getRejectReason());
        Assertions.assertThat(savedObject).isEqualTo(currentObject);
    }

    @Test
    void rejectObject_alreadyExistsException()
    {
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));
        currentObject.setStatus(Status.REJECTED);
        AlreadyExistsException thrownException = assertThrows(AlreadyExistsException.class, () -> {
            objserv.rejectObject(rejDTO);
        });
    }

    @Test
    void addObject()
    {
        when(modelMapper.map(Mockito.any(), Mockito.any()))
                .thenReturn(currentObject);
        given(dor.save(Mockito.any(DataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById(Mockito.anyString())).thenReturn(Optional.empty());
        DataObject savedObject = objserv.addObject(doDTO);
        Assertions.assertThat(savedObject).isEqualTo(currentObject);

    }


    @Test
    void addObject_alreadyExistsException()
    {
        when(dor.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(currentObject));
        AlreadyExistsException thrownException = assertThrows(AlreadyExistsException.class, () -> {
            objserv.addObject(doDTO);
        });
    }
}