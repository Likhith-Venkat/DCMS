package com.example.DCMS.controllerTests;


import com.example.DCMS.controllers.ObjectController;
import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
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
public class dataObjectControllerTest
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
        mp.put("cookies", "abc");
        Date dt = new Date();
        currentObject = dataObject.builder()
                .method("get")
                .uri("/api/abc")
                .requestHeaders(mp)
                .userEmail("abc@gmail.com")
                .username("abc")
                .data("data")
                .createdDate(dt)
                .status("PENDING")
                .objectType("BIN")
                .build();
    }



    @Test
    public void  get_ReturnsDataObjectList() throws Exception {
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
    public void shouldSubmitRequestToServiceOnApproval() throws Exception {
        // form approve request

        // mock backend service

        // approve the request

        // assert mock called with required info

        // assert on updating backend the approval is stored in db
    }

    @Test
    public void  addobj_ReturnsDataObject() throws Exception {
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
    public void  rejectObj_ReturnsDataObject() throws Exception {
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
