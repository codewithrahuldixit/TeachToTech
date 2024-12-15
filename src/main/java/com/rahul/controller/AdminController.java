package com.rahul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.model.Transactions;
import com.rahul.service.TransactionService;

@RestController
@RequestMapping("/api/admin/")
public class AdminController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction/add")
    public ResponseEntity<?> addTransation(@RequestBody Transactions transaction){
        this.transactionService.addTransactions(transaction);
        return ResponseEntity.ok("Transaction added successfully");
    }
    
    @PostMapping("/transaction/check")
    public ResponseEntity<?> checkTransaction(@RequestBody Transactions transaction){
        this.transactionService.sendPaymentNotification(transaction.getTransactionId());
        return ResponseEntity.ok("User done payment successfully");
    }

}
