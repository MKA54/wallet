package com.example.wallet.controller;

import com.example.wallet.dto.Update;
import com.example.wallet.dto.WalletDto;
import com.example.wallet.service.IWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
@Tag(name = "Кошелёк")
public class WalletController {
    private final IWalletService walletService;

    @PostMapping("create")
    @Operation(summary = "Создать")
    public ResponseEntity<?> createWallet() {
        UUID id = walletService.createWallet();
        return new ResponseEntity<>("Кошёлек успешно создан: " + id, HttpStatus.OK);
    }

    @PostMapping("wallet")
    @Operation(summary = "Обновить")
    public ResponseEntity<?> updateWallet(@RequestBody @Validated(Update.class) WalletDto walletDto) {
        WalletDto updateWalletDto = walletService.updateWallet(walletDto);
        return new ResponseEntity<>("Кошёлек обновлен: " + updateWalletDto, HttpStatus.OK);
    }

    @GetMapping("wallets/{id}")
    @Operation(summary = "Получить")
    public ResponseEntity<?> recoveryPassword(@PathVariable("id")  @Valid UUID id) {
        WalletDto walletDto = walletService.getWallet(id);
        return new ResponseEntity<>("Кошёлек: " + walletDto, HttpStatus.OK);
    }
}