package com.ungspp1.gadminbackend.api.priceHistory;


 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ungspp1.gadminbackend.restResponse.BaseBodyResponse;
 import com.ungspp1.gadminbackend.restResponse.ResponseHelper;

 @RestController
 @CrossOrigin(origins = "*")
 @RequestMapping("/api/v1/price")
 public class PriceController {
    
     @Autowired
     PriceHistoryFacade facade;

     @GetMapping(value = "/getAll", produces = {"application/json"})
     public ResponseEntity<BaseBodyResponse<?>> getAll(){
         try{
             return ResponseHelper.simpleResponse(facade.getAll());
         }  catch (Exception ex) {
             return ResponseHelper.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
         }
     }
 }
