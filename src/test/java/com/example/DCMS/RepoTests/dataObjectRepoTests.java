package com.example.DCMS.RepoTests;


import com.example.DCMS.models.dataObject;
import com.example.DCMS.repositories.dataObjectRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DataMongoTest
public class dataObjectRepoTests
{
    @Autowired
    private dataObjectRepo dor;
    private dataObject currentObject;

    @BeforeEach
    public void init()
    {

        currentObject = dataObject.builder()
                .userEmail("abc@gmail.com")
                .username("abc")
                .data("data")
                .objectType("BIN")
                .uniqueName("38271398721")
                .build();
    }

    @Test
    public void save_ReturnsDataObject() {
        //Act
        dataObject savedObject = dor.save(currentObject);

        //Assert
        Assertions.assertThat(savedObject).isNotNull();
        Assertions.assertThat(savedObject.getUsername()).isEqualTo(currentObject.getUsername());
    }

    @Test
    public void findByID_ReturnsDataObject() {
        //Act
        dataObject savedObject = dor.save(currentObject);
        dataObject returnedObject = dor.findById(savedObject.getId()).get();
        //Assert
        Assertions.assertThat(returnedObject).isNotNull();
        Assertions.assertThat(returnedObject.getUsername()).isEqualTo(savedObject.getUsername());
    }

    @Test
    public void findByStatusAndObjectType_ReturnsDataObjectList() {
        //Act
        dataObject savedObject = dor.save(currentObject);
        List<dataObject> returnedObject = dor.findByStatusAndObjectType("PENDING", "BIN");
        //Assert
        Assertions.assertThat(returnedObject).isNotNull();
        Assertions.assertThat(returnedObject.size()).isEqualTo(1);
    }

    @AfterEach
    public void exit()
    {
        dor.deleteAll();
    }
}
