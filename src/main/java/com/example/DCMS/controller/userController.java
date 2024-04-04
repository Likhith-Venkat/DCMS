package com.example.DCMS.controller;

import com.example.DCMS.config.MyUserDetails;
import com.example.DCMS.model.User;
import com.example.DCMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class userController
{
    @Autowired
    UserRepository ur;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping(path = "/getallusers")
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> res = new ArrayList<>(ur.findAll());
        if(res.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @GetMapping(path = "/sayhi")
    public String sayhi(Authentication auth)
    {
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return userDetails.getUsername()+userDetails.getAuthorities();


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
    @PostMapping(path="/public/adduser")
    public  ResponseEntity<User> addUser(@RequestBody User newUser)
    {
        String hashpswd = passwordEncoder.encode(newUser.getPassword());
        String Access = newUser.getAccess().toUpperCase();
        String type = newUser.getType().toUpperCase();
        String mctype = newUser.getMc_type().toUpperCase();
        newUser.setType(type);
        newUser.setAccess(Access);
        newUser.setMc_type(mctype);
        newUser.setPassword(hashpswd);
        User crtdUser = ur.save(newUser);
        return new ResponseEntity<>(crtdUser, HttpStatus.CREATED);
    }

    @GetMapping(path = "/deletebyid/{id}")
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
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: You do not have permission to access this resource.");
    }
}
