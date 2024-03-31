package com.example.DCMS.controller;

import com.example.DCMS.model.User;
import com.example.DCMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class userController
{
    @Autowired
    UserRepository ur;

    @GetMapping(path = "/public/getallusers")
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> res = new ArrayList<>(ur.findAll());
        if(res.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }
        System.out.println("HELLO");
        return new ResponseEntity<>(res, HttpStatus.OK);

    }
    @GetMapping(path = "/getuserbyid/{username}")
    public ResponseEntity<User> getUserById(@PathVariable String username)
    {
        Optional<User> res = ur.findById(username);
        if(res.isPresent())
        {
            return new ResponseEntity<>(res.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping(path="/adduser")
    public  ResponseEntity<User> addUser(@RequestBody User newUser)
    {
        User crtdUser = ur.save(newUser);
        return new ResponseEntity<>(crtdUser, HttpStatus.CREATED);
    }

    @GetMapping(path = "deletebyid/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable String id)
    {
        try
        {
            ur.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
