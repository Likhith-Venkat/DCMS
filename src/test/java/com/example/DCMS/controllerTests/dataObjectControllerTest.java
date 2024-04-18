package com.example.DCMS.controllerTests;


import com.example.DCMS.controllers.ObjectController;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import com.example.DCMS.DTOs.binDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockitoSession;
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
    private dataObject currentObject;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public void init()
    {
        Map<String, String> mp = new HashMap<>();
        mp.put("X-TENANT-ID", "MAHESHBANK");
        mp.put("Content-Type", "application/json");
        binDTO dt = binDTO.builder()
                .binValue("43243218")
                .checkSIExternal(false)
                .billingCurrency("356")
                .status(true)
                .binType("DEBIT")
                .build();
        currentObject = dataObject.builder()
                .id("1234")
                .method("POST")
                .uri("https://uat-dcms.m2pfintech.com/dcms-authnt/api/bins")
                .requestHeaders(mp)
                .userEmail("abc@gmail.com")
                .username("abc")
                .data(dt)
                .status("PENDING")
                .objectType("BIN")
                .build();
    }



    @Test
    void  get_ReturnsDataObjectList() throws Exception {
        List<dataObject> currentList = new ArrayList<>();
        currentList.add(currentObject);
        when(dor.findByStatusAndObjectType("PENDING", "BIN")).thenReturn(currentList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "1234");
        ResultActions response = mockMvc.perform(get("/mc/get/PENDING/BIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currentObject)));

        String expectedJson = objectMapper.writeValueAsString(currentList);
        response.andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void approve_returnsBackendResponse() throws Exception
    {
        when(dor.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(currentObject));
        given(dor.save(Mockito.any(dataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));
        JSONObject req = new JSONObject();
        req.put("id", "1234");
        ResultActions response = mockMvc.perform(put("/mc/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req.toString()));

        String expectedJson = objectMapper.writeValueAsString(currentObject.getData());
        System.out.println(response.toString());
        response.andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void  addobj_ReturnsDataObject() throws Exception {
        when(dor.save(Mockito.any(dataObject.class))).thenReturn(currentObject);

        ResultActions response = mockMvc.perform(post("/mc/addobj")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currentObject)));

        String expectedJson = objectMapper.writeValueAsString(currentObject);
        System.out.println(response.toString());
        response.andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }
    @Test
    void  rejectObj_ReturnsDataObject() throws Exception {
        JSONObject req = new JSONObject();
        req.put("id", "1234");
        req.put("rejectReason", "bad cred");

        when(dor.findById("1234")).thenReturn(Optional.ofNullable(currentObject));
        given(dor.save(Mockito.any(dataObject.class))).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(put("/mc/rejectobj")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req.toString()));
        currentObject.setRejectReason("bad cred");
        currentObject.setStatus("REJECTED");

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
