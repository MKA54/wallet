package com.example.wallet.service;

import com.example.wallet.dto.WalletDto;

import java.util.UUID;

public interface IWalletService {
    WalletDto updateWallet(WalletDto walletDto);

    WalletDto getWallet(UUID id);

    UUID createWallet();
}