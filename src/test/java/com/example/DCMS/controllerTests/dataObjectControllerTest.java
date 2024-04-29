package com.example.DCMS.controllerTests;


import com.example.DCMS.DTOs.approveDTO;
import com.example.DCMS.DTOs.dataObjectDTO;
import com.example.DCMS.DTOs.rejectDTO;
import com.example.DCMS.controllers.ObjectController;
import com.example.DCMS.services.ObjectServiceImpl;
import com.example.DCMS.enums.Status;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import com.example.DCMS.DTOs.binDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ObjectController.class)
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@ExtendWith(MockitoExtension.class)
class dataObjectControllerTest
{
    @MockBean
    private dataObjectRepo dor;
    @MockBean
    private ObjectServiceImpl objServ;
    private dataObject currentObject;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private dataObjectDTO doDTO;
    private approveDTO apprDTO;
    private rejectDTO rejDTO;
    @BeforeEach
    public void init()
    {
        binDTO dt = binDTO.builder()
                .binValue("764673")
                .checkSIExternal(false)
                .billingCurrency("356")
                .status(true)
                .binType("DEBIT")
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
        doDTO = dataObjectDTO.builder()
                .userEmail("abc@gmail.com")
                .username("abc")
                .data(dt)
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
    }



    @Test
    void  get_retursList() throws Exception {
        List<dataObject> currentList = new ArrayList<>();
        currentList.add(currentObject);
        when(dor.findByStatusAndObjectType("PENDING", "BIN")).thenReturn(currentList);
        ResultActions response = mockMvc.perform(get("/mc/get/PENDING/BIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currentObject)));

        String expectedJson = objectMapper.writeValueAsString(currentList);
        response.andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void approve() throws Exception
    {
        currentObject.setStatus(Status.APPROVED);
        when(objServ.approveObject(Mockito.any(approveDTO.class), Mockito.any(HttpHeaders.class))).thenReturn(currentObject);
        ResultActions response = mockMvc.perform(put("/mc/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apprDTO))
                .header("X-TENANT-ID", "MAHESHBANK")
                .header("content-type", "application/json"));

        String expectedJson = objectMapper.writeValueAsString(currentObject);
        System.out.println(response.toString());
        response.andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void approve_ReturnsRejectedObject() throws Exception
    {
        currentObject.setStatus(Status.REJECTED);
        when(objServ.approveObject(Mockito.any(approveDTO.class), Mockito.any(HttpHeaders.class))).thenReturn(currentObject);
        ResultActions response = mockMvc.perform(put("/mc/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apprDTO))
                .header("X-TENANT-ID", "MAHESHBANK")
                .header("content-type", "application/json"));

        String expectedJson = objectMapper.writeValueAsString(currentObject);
        System.out.println(response.toString());
        response.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void  addobj() throws Exception {
        when(objServ.addObject(Mockito.any(dataObjectDTO.class))).thenReturn(currentObject);

        ResultActions response = mockMvc.perform(post("/mc/addobj")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doDTO)));

        String expectedJson = objectMapper.writeValueAsString(currentObject);
        System.out.println(response.toString());
        response.andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }
    @Test
    void  reject() throws Exception {

        currentObject.setStatus(Status.REJECTED);
        currentObject.setRejectReason(rejDTO.getRejectReason());
        when(objServ.rejectObject(Mockito.any(rejectDTO.class))).thenReturn(currentObject);

        ResultActions response = mockMvc.perform(put("/mc/rejectobj")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rejDTO)));


        String expectedJson = objectMapper.writeValueAsString(currentObject);
        System.out.println(response.toString());
        response.andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }


    @AfterEach
    public void exit()
    {
        dor.deleteAll();
    }
}
