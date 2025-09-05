package com.example.wallet.dao;

import com.example.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository
        extends CrudRepository<Wallet, UUID>,
        JpaSpecificationExecutor<Wallet> {
}