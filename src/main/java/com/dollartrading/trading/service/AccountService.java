package com.dollartrading.trading.service;

import com.dollartrading.trading.dto.AccountDto;
import com.dollartrading.trading.dto.OperationStatusDto;
import com.dollartrading.trading.exceptions.EntityAddingException;
import com.dollartrading.trading.exceptions.EntityAlreadyExistException;
import com.dollartrading.trading.exceptions.EntityNotFoundException;
import com.dollartrading.trading.exceptions.EntityUpdatingException;
import com.dollartrading.trading.models.Account;
import com.dollartrading.trading.repos.AccountRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class AccountService {
    private final AccountRepo accountRepo;

    @Autowired
    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    private Account dtoToEntity(AccountDto accountDto) {
        return Account.builder()
                .paymentName(accountDto.getPaymentName())
                .password(accountDto.getPassword())
                .username(accountDto.getUserName())
                .build();
    }

    public OperationStatusDto addAccount(AccountDto accountDto) throws EntityAddingException, EntityAlreadyExistException {
        if (accountRepo.findAccountByUsername(accountDto.getUserName()) != null) {
            throw new EntityAlreadyExistException(Messages.ADDED_EXCEPTION_MESSAGE.getMessage());
        }
        try {
            return generateUpdatingMessage(dtoToEntity(accountDto), Messages.ADDED_MESSAGE);
        } catch (Exception e) {
            log.error(Messages.ERROR_SAVING_ENTITY.getMessage(), e);
            throw new EntityAddingException(e);
        }
    }

    public OperationStatusDto updateAccount(AccountDto accountDto, Long id) throws EntityNotFoundException, EntityUpdatingException {
        Optional<Account> accountFromDb = accountRepo.findById(id);
        Account updatingAccount = updateAccount(getOldAccount(accountFromDb), accountDto);
        return generateUpdatingMessage(updatingAccount, Messages.UPDATED_MESSAGE);
    }

    public void deleteAccount(Long id) {
        accountRepo.deleteById(id);
    }

    private Account getOldAccount(Optional<Account> accountFromDb) throws EntityNotFoundException {
        if (accountFromDb.isEmpty()) {
            log.error(Messages.NOT_FOUND_MESSAGE.getMessage());
            throw new EntityNotFoundException(Messages.NOT_FOUND_MESSAGE.getMessage());
        }
        return accountFromDb.get();
    }

    private Account updateAccount(Account oldAccount, AccountDto accountDto){
        oldAccount.setPaymentName(accountDto.getPaymentName());
        oldAccount.setPassword(accountDto.getPassword());
        oldAccount.setUsername(accountDto.getUserName());
        return oldAccount;
    }

    private OperationStatusDto generateUpdatingMessage(Account updatingAccount, Messages message) throws EntityUpdatingException {
        try {
            return OperationStatusDto.builder()
                    .id(accountRepo.save(updatingAccount).getId())
                    .message(message.getMessage())
                    .build();
        } catch (Exception e) {
            log.error(Messages.ERROR_SAVING_ENTITY.getMessage());
            throw new EntityUpdatingException(e);
        }
    }
}
