package com.example.DCMS.controller;

import com.example.DCMS.config.MyUserDetails;
import com.example.DCMS.model.MakerCheckerRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/auth/mc")
public class MakerCheckerController {

    @Autowired
    MongoOperations mongoOperations;

    private final WebClient webClient;
    @Autowired
    public MakerCheckerController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    @PostMapping("/{uri}")
    public ResponseEntity<?> processMakerCheckerRequest(@PathVariable String uri, @RequestBody MakerCheckerRequest request) {
        try {
            validateRequest(request);
            MyUserDetails userDetails = getCurrentUserDetails();
            request.setMadeDate(new Date());
            request.setStatus("PENDING");
            request.setMaker(userDetails.getUsername());
            request.setUri(uri);
            Document requestDocument = convertToDocument(request);
            mongoOperations.save(requestDocument, "maker_checker_requests");
            return new ResponseEntity<>("Request successfully submitted for approval.", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to process the request.", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptMakerCheckerRequest(@PathVariable String id) {
        try {
            Document requestDocument = mongoOperations.findById(id, Document.class, "maker_checker_requests");
            if (requestDocument == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found.");
            }
            requestDocument.put("status", "ACCEPTED");
            MyUserDetails userDetails = getCurrentUserDetails();
            requestDocument.put("checker", userDetails.getUsername());
            requestDocument.put("checkedDate", new Date());
            mongoOperations.save(requestDocument, "maker_checker_requests");
            forwardRequest(id);
            return new ResponseEntity<>("Request accepted.", HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to accept the request.", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectMakerCheckerRequest(@PathVariable String id) {
        try {
            Document requestDocument = mongoOperations.findById(id, Document.class, "maker_checker_requests");
            if (requestDocument == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found.");
            }
            requestDocument.put("status", "REJECTED");
            MyUserDetails userDetails = getCurrentUserDetails();
            requestDocument.put("checker", userDetails.getUsername());
            requestDocument.put("checkedDate", new Date());
            mongoOperations.save(requestDocument, "maker_checker_requests");
            return new ResponseEntity<>("Request rejected.", HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to reject the request.", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    @PostMapping("/forward/{id}")
    public ResponseEntity<?> forwardRequest(@PathVariable String id) {
        try {
            Document requestDocument = mongoOperations.findById(id, Document.class, "maker_checker_requests");
            if (requestDocument == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found.");
            }
            String status = requestDocument.getString("status");
            if (!status.equals("ACCEPTED")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is not accepted.");
            }
            String method = requestDocument.getString("method");
            String uri = requestDocument.getString("uri");
            String requestBody = requestDocument.getString("requestBody");
            forwardHttpRequest(method, uri, requestBody);
            return ResponseEntity.ok("Request forwarded.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to forward the request.", e);
        }
    }


    private void validateRequest(MakerCheckerRequest request) {
        if (request.getMethod() == null || request.getUri() == null || request.getRequestBody() == null
                || request.getObjectType() == null) {
            throw new IllegalArgumentException("Invalid request: Missing required fields.");
        }
    }

    private MyUserDetails getCurrentUserDetails() {
        //gets user details from the /auth middleware containing user credentials
        return null; // Placeholder implementation
    }

    private Document convertToDocument(MakerCheckerRequest request) {
        Document document = new Document();
        document.put("method", request.getMethod());
        document.put("uri", request.getUri());
        document.put("requestBody", request.getRequestBody());
        document.put("maker", request.getMaker());
        document.put("status", request.getStatus());
        document.put("objectType", request.getObjectType());
        return document;
    }

    private void forwardHttpRequest(String method, String uri, String requestBody) {
        try {
            switch (method.toUpperCase()) {
                case "GET":
                    webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
                    break;
                case "POST":
                    webClient.post().uri(uri).body(BodyInserters.fromValue(requestBody)).retrieve().bodyToMono(String.class).block();
                    break;
                case "PUT":
                    webClient.put().uri(uri).body(BodyInserters.fromValue(requestBody)).retrieve().bodyToMono(String.class).block();
                    break;
                case "DELETE":
                    webClient.delete().uri(uri).retrieve().bodyToMono(String.class).block();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported HTTP method: " + method);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to forward the request: " + e.getMessage());
        }
    }
    
}
