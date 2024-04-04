package com.example.DCMS.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class objcheckers {
    @Autowired
    MongoOperations mongoOperations;

    public void ifChannelLimitAddToProduct(Document res) {
        if (Objects.equals(res.getString("objectType"), "CHANNELLIMIT")) {
            String productName = res.getString("productName");
            Criteria cr = Criteria.where("objectType").is("PRODUCT").and("productName").is(productName);
            Query qr = new Query(cr);
            Document productDoc = mongoOperations.findOne(qr, Document.class, "mcobjects");
            assert productDoc != null;
            productDoc.put("channelLimit", res);
            mongoOperations.save(productDoc, "mcobjects");
        }

    }

    public void ifFeeConfigAddToProduct(Document res) {
        if (Objects.equals(res.getString("objectType"), "FEECONFIG")) {
            String productName = res.getString("productName");
            Criteria cr = Criteria.where("objectType").is("PRODUCT").and("productName").is(productName);
            Query qr = new Query(cr);
            Document productDoc = mongoOperations.findOne(qr, Document.class, "mcobjects");
            assert productDoc != null;
            productDoc.put("feeConfig", res);
            mongoOperations.save(productDoc, "mcobjects");
        }

    }

    public Boolean isUnique(String objectType, String value)
    {

        Criteria cr = Criteria.where("objectType").is(objectType).and(objectType).is(value);
        Query qr = new Query(cr);
        if(mongoOperations.findOne(qr, Document.class, "mcobjects")!= null) {
            return false;
        }
        return true;
    }
    public Boolean exists(String objectType, String key, String value)
    {
        Criteria cr1 = Criteria.where("objectType").is(objectType).and(key).is(value);
        Query qr1 = new Query(cr1);
        if(mongoOperations.findOne(qr1, Document.class, "mcobjects")== null)
        {
            return false;
        }
        return true;
    }

    public Boolean checkUniqueObjs(Document doc)
    {
        String UniqueID = doc.getString("objectType");
        boolean rtn = isUnique(UniqueID, doc.getString(UniqueID));
        return rtn;
//        Criteria criteria = Criteria.where(doc.getString("Uni"))
//        Boolean rtn = false;
//        switch(doc.getString("objectType")) {
//            case "BIN":
//                rtn = isUnique("BIN", "bin", doc.getString("bin"));
//            case "BINRANGE":
//                rtn = exists("BIN", "bin", doc.getString("bin")) && isUnique("BINRANGE", "binrangeName", doc.getString("binrangeName"));
//            case "PRODUCT":
//                rtn = isUnique("PRODUCT", "productName", doc.getString("productName"));
//                break;
//            case "CHANNELLIMIT":
//                rtn = exists("PRODUCT", "productName", doc.getString("productName"));
//                break;
//            case "FEECONFIG":
//                rtn = exists("PRODUCT", "productName", doc.getString("productName"));
//                break;
//        }
//        return rtn;
    }
}
