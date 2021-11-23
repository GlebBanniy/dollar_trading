package com.dollartrading.trading.service;

import com.dollartrading.trading.dto.AccountDto;
import com.dollartrading.trading.dto.SuccessfulUpdateDto;
import com.dollartrading.trading.exceptions.AccountAddingException;
import com.dollartrading.trading.exceptions.AccountAlreadyExistException;
import com.dollartrading.trading.exceptions.AccountNotFoundMyException;
import com.dollartrading.trading.exceptions.AccountUpdatingException;
import com.dollartrading.trading.models.Account;
import com.dollartrading.trading.repos.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepo accountRepo;
    private static final String UPDATED_MESSAGE = "successfully updated";
    private static final String ADDED_EXCEPTION_MESSAGE = "This account is already exist";
    private static final String NOT_FOUND_MESSAGE = "This account does not exist";
    @Autowired
    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    private Account dtoToEntity(AccountDto accountDto) {
        return Account.builder()
                .paymentName(accountDto.getPaymentName())
                .password(accountDto.getPassword())
                .username(accountDto.getUsername())
                .build();
    }

    public Long addAccount(AccountDto accountDto) throws AccountAddingException, AccountAlreadyExistException {
        if (accountRepo.findAccountByUsername(accountDto.getUsername()) != null) {
            throw new AccountAlreadyExistException(ADDED_EXCEPTION_MESSAGE);
        }
        try {
            return accountRepo.save(dtoToEntity(accountDto)).getId();
        } catch (Exception e) {
            throw new AccountAddingException(e);
        }
    }

    public SuccessfulUpdateDto updateAccount(AccountDto accountDto, Long id) throws AccountNotFoundMyException, AccountUpdatingException {
        Optional<Account> accountFromDb = accountRepo.findById(id);
        if (accountFromDb.isEmpty()) {
            throw new AccountNotFoundMyException(NOT_FOUND_MESSAGE);
        }
        accountFromDb.get().setPaymentName(accountDto.getPaymentName());
        accountFromDb.get().setPassword(accountDto.getPassword());
        accountFromDb.get().setUsername(accountDto.getUsername());
        try {
            return SuccessfulUpdateDto.builder()
                    .id(accountRepo.save(dtoToEntity(accountDto)).getId())
                    .message(UPDATED_MESSAGE)
                    .build();
        } catch (Exception e) {
            throw new AccountUpdatingException(e);
        }
    }

    public void deleteAccount(Long id) {
        accountRepo.deleteById(id);
    }

}
