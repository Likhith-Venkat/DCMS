package com.example.DCMS.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/mc")
public class objectControllers
{
    @Autowired
    MongoOperations mongoOperations;


    @GetMapping(path = "/test")
    public ResponseEntity<?> helloWorld()
    {
        JSONObject test = new JSONObject();
        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }


//    @PutMapping(path = "/approveobj")
//    public ResponseEntity<Document> approveobj(Authentication auth, @RequestBody String req)
//    {
//        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//
//        try {
//            JSONObject jsonReq = new JSONObject(req);
//            String id =jsonReq.getString("id");
//            Criteria cr = Criteria.where("_id").is(id);
//            Query qr = new Query(cr);
//            Document doc = mongoOperations.findOne(qr, Document.class, "mcobjects");
//            assert doc != null;
//            if(!Objects.equals(doc.getString("status"), "PENDING"))
//            {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object Already checked");
//            }
//            if(!objchks.checkUniqueObjs(doc))
//            {
//                doc.replace("status", "REJECTED");
//                doc.replace("rejectReason", "Accept conflict");
//                Document res = mongoOperations.save(doc, "mcobjects");
//                return new ResponseEntity<>(res, HttpStatus.CREATED);
//            }
//            doc.replace("status", "ACCEPTED");
//            doc.put("checker", userDetails.getUsername());
//
//            Document res = mongoOperations.save(doc, "mcobjects");
//
//            objchks.ifChannelLimitAddToProduct(res);
//            objchks.ifFeeConfigAddToProduct(res);
//            return new ResponseEntity<>(res, HttpStatus.CREATED);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
//        }
//    }
//    @PutMapping(path = "/rejectobj")
//    public ResponseEntity<?> rejectobj(Authentication auth,  @RequestBody String req)
//    {
//        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//
//        try {
//
//            JSONObject jsonReq = new JSONObject(req);
//            String id =jsonReq.getString("id");
//            String rejectReason = jsonReq.getString("rejectReason");
//            Criteria cr = Criteria.where("_id").is(id);
//            Query qr = new Query(cr);
//            Document doc = mongoOperations.findOne(qr, Document.class, "mcobjects");
//            if(!Objects.equals(doc.getString("status"), "PENDING"))
//            {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object Alreay checked");
//            }
//            assert doc != null;
//            doc.put("rejectReason", rejectReason);
//            doc.replace("status", "REJECTED");
//            doc.put("checker", userDetails.getUsername());
//            Document res = mongoOperations.save(doc, "mcobjects");
//
//            return new ResponseEntity<>(res, HttpStatus.CREATED);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
//        }
//    }
//    @PostMapping(path = "/addobj")
//    public ResponseEntity<Document> addobj(Authentication auth, @RequestBody String req)
//    {
//        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//
//        try {
//            JSONObject jsonReq = new JSONObject(req);
//            jsonReq.put("status", "PENDING");
//            jsonReq.put("maker", userDetails.getUsername());
//            Document document = Document.parse(jsonReq.toString());
//            document.put("_id", UUID.randomUUID().toString());
//            if(!objchks.checkUniqueObjs(document))
//            {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object already exists/wrong attributes");
//            }
//            Document res = mongoOperations.save(document, "mcobjects");
//            return new ResponseEntity<>(res, HttpStatus.CREATED);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body format");
//        }
//    }
//    @GetMapping(path = "/get")
//    public List<Document> get(Authentication auth, @RequestBody String request){
//        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//        try{
//            JSONObject jsonObject =  new JSONObject(request);
//            Criteria criteria = Criteria.where("status").is(jsonObject.get("status")).and("objectType").is(jsonObject.get("objectType"));
//            Query query = new Query(criteria);
//            return mongoOperations.find(query,Document.class, "mcobjects");
//        } catch (Exception e){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
//    }


}