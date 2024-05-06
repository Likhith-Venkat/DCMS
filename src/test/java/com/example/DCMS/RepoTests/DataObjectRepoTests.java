package com.example.DCMS.RepoTests;


import com.example.DCMS.enums.ObjectType;
import com.example.DCMS.enums.Status;
import com.example.DCMS.models.DataObject;
import com.example.DCMS.repositories.DataObjectRepo;
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
public class DataObjectRepoTests
{
    @Autowired
    private DataObjectRepo dor;
    private DataObject currentObject;

    @BeforeEach
    public void init()
    {

        currentObject = DataObject.builder()
                .userEmail("abc@gmail.com")
                .username("abc")
                .data("data")
                .objectType(ObjectType.BIN)
                .uniqueName("38271398721")
                .build();
    }

    @Test
    public void save_ReturnsDataObject() {
        //Act
        DataObject savedObject = dor.save(currentObject);

        //Assert
        Assertions.assertThat(savedObject).isNotNull();
        Assertions.assertThat(savedObject.getUsername()).isEqualTo(currentObject.getUsername());
    }

    @Test
    public void findByID_ReturnsDataObject() {
        //Act
        DataObject savedObject = dor.save(currentObject);
        DataObject returnedObject = dor.findById(savedObject.getId()).get();
        //Assert
        Assertions.assertThat(returnedObject).isNotNull();
        Assertions.assertThat(returnedObject.getUsername()).isEqualTo(savedObject.getUsername());
    }

    @Test
    public void findByStatusAndObjectType_ReturnsDataObjectList() {
        //Act
        DataObject savedObject = dor.save(currentObject);
        List<DataObject> returnedObject = dor.findByStatusAndObjectType(Status.PENDING, ObjectType.BIN);
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
