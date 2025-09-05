package com.example.wallet.service.impl;

import com.example.wallet.dao.WalletRepository;
import com.example.wallet.dto.WalletDto;
import com.example.wallet.model.Wallet;
import com.example.wallet.service.IWalletService;
import com.example.wallet.service.error.InsufficientFundsException;
import com.example.wallet.service.error.WalletNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {
    private final WalletRepository walletRepository;

    @Transactional
    @Override
    public WalletDto updateWallet(WalletDto walletDto) {
        return walletRepository.findById(walletDto.id())
                .map(wallet -> {
                    BigDecimal newAmount = switch (walletDto.operationType()) {
                        case DEPOSIT -> wallet.getAmount().add(walletDto.amount());
                        case WITHDRAW -> {
                            if (wallet.getAmount().subtract(walletDto.amount()).signum() == -1) {
                                throw new InsufficientFundsException(wallet.getAmount(), walletDto.id());
                            }

                            yield wallet.getAmount().subtract(walletDto.amount());
                        }
                    };

                    wallet.setAmount(newAmount);

                    Wallet savedWallet = walletRepository.save(wallet);
                    return new WalletDto(savedWallet.getId(), null, savedWallet.getAmount());
                })
                .orElseThrow(() -> new WalletNotFoundException(walletDto.id()));
    }

    @Transactional
    @Override
    public WalletDto getWallet(UUID id) {
        Optional<Wallet> optionalWallet = walletRepository.findById(id);

        return optionalWallet.map(wallet ->
                new WalletDto(wallet.getId(), null, wallet.getAmount())
        ).orElseThrow(() -> new WalletNotFoundException(id));
    }

    @Transactional
    @Override
    public UUID createWallet() {
        return walletRepository.save(new Wallet()).getId();
    }
}