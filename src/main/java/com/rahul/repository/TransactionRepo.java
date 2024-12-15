package com.rahul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.model.Transactions;

@Repository
public interface TransactionRepo extends JpaRepository<Transactions,Long> {

    public Optional<Transactions> findByTransactionId(String transactionId);
    
}
