package com.dollartrading.trading.service;

import com.dollartrading.trading.dto.AccountDto;
import com.dollartrading.trading.dto.OperationStatusDto;
import com.dollartrading.trading.exceptions.EntityAddingException;
import com.dollartrading.trading.exceptions.EntityAlreadyExistException;
import com.dollartrading.trading.exceptions.EntityNotFoundException;
import com.dollartrading.trading.exceptions.EntityUpdatingException;
import com.dollartrading.trading.models.Account;
import com.dollartrading.trading.repos.AccountRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepo accountRepo;
    private AutoCloseable autoCloseable;
    private AccountService accountService;
    private Account accountFromDb;
    private Account account;
    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        accountService = new AccountService(accountRepo);
        accountFromDb = Account.builder()
                .id(TestsVars.ID.getTestId())
                .password(TestsVars.PASSWORD.getString())
                .paymentName(TestsVars.BANK_NAME.getString())
                .username(TestsVars.NAME.getString())
                .build();
        account = Account.builder()
                .password(TestsVars.PASSWORD.getString())
                .paymentName(TestsVars.BANK_NAME.getString())
                .username(TestsVars.NAME.getString())
                .build();
        accountDto = new AccountDto(
                TestsVars.BANK_NAME.getString(),
                TestsVars.NAME.getString(),
                TestsVars.PASSWORD.getString());
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    private Account captureFinalVariable(){
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepo).save(accountArgumentCaptor.capture());
        return accountArgumentCaptor.getValue();
    }

    @Test
    void tryAddAccount() throws EntityAlreadyExistException, EntityAddingException {
        when(accountRepo.save(any(Account.class))).thenReturn(accountFromDb);
        OperationStatusDto result = accountService.addAccount(accountDto);

        Account value = captureFinalVariable();
        assertThat(value).isEqualTo(account);

        OperationStatusDto rightValue = OperationStatusDto.builder()
                        .id(TestsVars.ID.getTestId()).message(Messages.ADDED_MESSAGE.getMessage()).build();
        assertThat(result).isEqualTo(rightValue);
    }

    @Test
    void willThrowWhenEntityAlreadyExist() {
        given(accountRepo.findAccountByUsername(account.getUsername()))
                .willReturn(account);
        assertThatThrownBy(() -> accountService.addAccount(accountDto))
                .isInstanceOf(EntityAlreadyExistException.class)
                .hasMessageContaining(Messages.ADDED_EXCEPTION_MESSAGE.getMessage());
        verify(accountRepo, never()).save(any());
    }

    @Test
    void willThrowWhenEntitySavingError() {
        RuntimeException runtimeException = new RuntimeException();
        given(accountRepo.save(account))
                .willThrow(runtimeException);
        assertThatThrownBy(() -> accountService.addAccount(accountDto))
                .isInstanceOf(EntityAddingException.class)
                .hasMessageContaining(String.valueOf(runtimeException));
    }

    @Test
    void tryUpdateAccount() throws EntityUpdatingException, EntityNotFoundException {
        when(accountRepo.save(any(Account.class))).thenReturn(accountFromDb);
        when(accountRepo.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(accountFromDb));
        OperationStatusDto result = accountService.updateAccount(accountDto, TestsVars.ID.getTestId());

        Account value = captureFinalVariable();
        assertThat(value).isEqualTo(account);

        OperationStatusDto rightValue = OperationStatusDto.builder()
                .id(TestsVars.ID.getTestId()).message(Messages.UPDATED_MESSAGE.getMessage()).build();
        assertThat(result).isEqualTo(rightValue);
    }

    @Test
    void willThrowWhenEntityNotFound() {
        Optional<Account> accountOptional = Optional.empty();
        given(accountRepo.findById(TestsVars.ID.getTestId()))
                .willReturn(accountOptional);
        assertThatThrownBy(() -> accountService.updateAccount(accountDto, TestsVars.ID.getTestId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(Messages.NOT_FOUND_MESSAGE.getMessage());
        verify(accountRepo, never()).save(any());
    }
}
