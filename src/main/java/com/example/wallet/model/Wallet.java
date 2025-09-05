package com.example.wallet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Wallet {
    @Id
    @UuidGenerator
    @NotNull
    UUID id;
    @Column(precision = 19, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp created_at;
    @Version
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp version;

    public Wallet(UUID id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }
}