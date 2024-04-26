package com.example.DCMS.services;

import com.example.DCMS.DTOs.*;
import com.example.DCMS.enums.Status;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@AutoConfigureDataMongo
@ExtendWith(MockitoExtension.class)
class ObjectServiceImplTest {

    @Mock
    private dataObjectRepo dor;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RestTemplate restTemplate;
    private dataObject currentObject;
    private dataObjectDTO doDTO;
    private rejectDTO rejDTO;
    private approveDTO apprDTO;




    private binDTO dt;
    HttpHeaders headers = new HttpHeaders();
    @InjectMocks
    ObjectServiceImpl objserv;



    @BeforeEach
    void setUp() {
        dt = binDTO.builder()
                .binValue("764673")
                .checkSIExternal(false)
                .billingCurrency("356")
                .status(true)
                .binType("DEBIT")
                .build();
        doDTO = dataObjectDTO.builder()
                .userEmail("abc@gmail.com")
                .username("abc")
                .data(dt)
                .objectType("BIN")
                .uniqueName("764673")
                .build();
        currentObject = dataObject.builder()
                .id("764673BIN")
                .userEmail("abc@gmail.com")
                .username("abc")
                .data(dt)
                .status(Status.PENDING)
                .objectType("BIN")
                .uniqueName("764673")
                .build();
        rejDTO = rejectDTO.builder()
                .rejectReason("Bad credentials")
                .id("764673BIN")
                .build();
        apprDTO = approveDTO.builder()
                .url("https://uat-dcms.m2pfintech.com/dcms-authnt/api/bins")
                .method("POST")
                .id("764673BIN")
                .build();
        headers.add("X-TENANT-ID", "MAHESHBANK");
        headers.add("content-type", "application/json");

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void approveObject_returnsOK()
    {
        given(dor.save(Mockito.any(dataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));

        ResponseEntity<String> responseEntity = new ResponseEntity<>("done", HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(responseEntity);
        dataObject res = objserv.approveObject(apprDTO, headers);
        currentObject.setStatus(Status.APPROVED);
        System.out.println(res);
        Assertions.assertThat(res).isEqualTo(currentObject);
    }

    @Test
    void approveObject_wrongMethod()
    {
        given(dor.save(Mockito.any(dataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));
        apprDTO.setMethod("JKJDSA");
        ResponseEntity<String> responseEntity = new ResponseEntity<>("done", HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(responseEntity);
        dataObject res = objserv.approveObject(apprDTO, headers);
        currentObject.setStatus(Status.APPROVED);
        System.out.println(res);
        Assertions.assertThat(res).isEqualTo(currentObject);
    }
    @Test
    void approveObject_throwsBadRequest()
    {
        given(dor.save(Mockito.any(dataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Req, Wrong Bin");

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenThrow(exception);
        dataObject res = objserv.approveObject(apprDTO, headers);
        currentObject.setStatus(Status.REJECTED);
        currentObject.setRejectReason("Bad Req, Wrong Bin");
        System.out.println(res);
        Assertions.assertThat(res).isEqualTo(currentObject);
    }
    @Test
    void approveObject_returnsBadRequest()
    {
        given(dor.save(Mockito.any(dataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Bad Req, Wrong Bin", HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(responseEntity);
        dataObject res = objserv.approveObject(apprDTO, headers);
        currentObject.setStatus(Status.REJECTED);
        currentObject.setRejectReason("Bad Req, Wrong Bin");
        System.out.println(res);
        Assertions.assertThat(res).isEqualTo(currentObject);
    }

    @Test
    void rejectObject()
    {
        given(dor.save(Mockito.any(dataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById("764673BIN")).thenReturn(Optional.ofNullable(currentObject));
        dataObject savedObject = objserv.rejectObject(rejDTO);
        currentObject.setStatus(Status.REJECTED);
        currentObject.setRejectReason(rejDTO.getRejectReason());
        Assertions.assertThat(savedObject).isEqualTo(currentObject);
    }

    @Test
    void addObject()
    {
        given(dor.save(Mockito.any(dataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        when(dor.findById(Mockito.anyString())).thenReturn(Optional.empty());
        dataObject savedObject = objserv.addObject(doDTO);
        Assertions.assertThat(savedObject).isEqualTo(currentObject);
    }
}