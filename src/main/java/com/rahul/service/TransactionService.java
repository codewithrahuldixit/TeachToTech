package com.rahul.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.rahul.model.Transactions;
import com.rahul.repository.TransactionRepo;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private JavaMailSender emailSender;

    private static final String email="keshchaurasiya448@gmail.com";

    public void addTransactions(Transactions transaction){
        this.transactionRepo.save(transaction);
    }

    public void sendPaymentNotification(String transactionId) {
        // Find the payment by transactionId
        Optional<Transactions> transaction = transactionRepo.findByTransactionId(transactionId);
        
        if (transaction.isPresent()) {
            Transactions transactions=transaction.get();
            // Send email notification about the payment success
            sendEmail(email, "Payment Successful", 
                      "Your payment of " + transactions.getTransactionId() + " was successful.");
        }
        else{
            sendEmail(email,"Payment Failure","Sorry this transaction id "+transactionId+" not found");
        } 
    
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);  // Send the email
    }
    
}
