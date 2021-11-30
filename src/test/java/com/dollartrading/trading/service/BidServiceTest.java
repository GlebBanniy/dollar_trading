package com.dollartrading.trading.service;

import com.dollartrading.trading.dto.BidDto;
import com.dollartrading.trading.dto.OperationStatusDto;
import com.dollartrading.trading.exceptions.EntityAddingException;
import com.dollartrading.trading.exceptions.EntityNotFoundException;
import com.dollartrading.trading.exceptions.EntityUpdatingException;
import com.dollartrading.trading.models.Account;
import com.dollartrading.trading.models.Bid;
import com.dollartrading.trading.repos.AccountRepo;
import com.dollartrading.trading.repos.BidRepo;
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
class BidServiceTest {

    @Mock
    private BidRepo bidRepo;

    @Mock
    private AccountRepo accountRepo;

    private AutoCloseable autoCloseable;
    private BidService bidService;
    private Bid bidFromDb;
    private Bid bid;
    private BidDto bidDto;
    private Account accountFromDb;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        bidService = new BidService(bidRepo, accountRepo);
        accountFromDb = Account.builder()
                .id(TestsVars.ID.getTestId())
                .password(TestsVars.PASSWORD.getString())
                .paymentName(TestsVars.BANK_NAME.getString())
                .username(TestsVars.NAME.getString())
                .build();
        bidFromDb = Bid.builder()
                .id(TestsVars.ID.getTestId())
                .bidValue(TestsVars.BID_VALUE.getValue())
                .currency(TestsVars.CURRENCY.getString())
                .account(accountFromDb)
                .isActive(true)
                .build();
        bid = Bid.builder()
                .bidValue(TestsVars.BID_VALUE.getValue())
                .currency(TestsVars.CURRENCY.getString())
                .account(accountFromDb)
                .isActive(true)
                .build();
        bidDto = new BidDto(
                TestsVars.NAME.getString(),
                TestsVars.CURRENCY.getString(),
                TestsVars.BID_VALUE.getValue(),
                true);
    }

    private Bid captureFinalVariable(){
        var bidArgumentCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepo).save(bidArgumentCaptor.capture());
        return bidArgumentCaptor.getValue();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void tryAddBid() throws EntityAddingException {
        when(accountRepo.findAccountByUsername(any(String.class))).thenReturn(accountFromDb);
        when(bidRepo.save(any(Bid.class))).thenReturn(bidFromDb);
        var result = bidService.addBid(bidDto);

        var value = captureFinalVariable();
        assertThat(value).isEqualTo(bid);

        var rightValue = OperationStatusDto.builder()
                .id(TestsVars.ID.getTestId()).message(Messages.ADDED_MESSAGE.getMessage()).build();
        assertThat(result).isEqualTo(rightValue);
    }

    @Test
    void willThrowWhenEntityAlreadyExist() {
        given(bidRepo.save(bid))
                .willThrow(RuntimeException.class);
        assertThatThrownBy(() -> bidService.addBid(bidDto))
                .isInstanceOf(EntityAddingException.class)
                .hasMessageContaining(Messages.ERROR_SAVING_ENTITY.getMessage());
    }

    @Test
    void tryUpdateBid() throws EntityUpdatingException, EntityNotFoundException {
        when(bidRepo.save(any(Bid.class))).thenReturn(bidFromDb);
        when(bidRepo.findById(any(Long.class))).thenReturn(Optional.ofNullable(bidFromDb));
        var result = bidService.updateBid(bidDto, TestsVars.ID.getTestId());

        var value = captureFinalVariable();
        assertThat(value).isEqualTo(bid);

        OperationStatusDto rightValue = OperationStatusDto.builder()
                .id(TestsVars.ID.getTestId()).message(Messages.UPDATED_MESSAGE.getMessage()).build();
        assertThat(result).isEqualTo(rightValue);
    }

    @Test
    void willThrowWhenEntityNotFound() {
        Optional<Bid> bidOptional = Optional.empty();
        given(bidRepo.findById(TestsVars.ID.getTestId()))
                .willReturn(bidOptional);
        assertThatThrownBy(() -> bidService.updateBid(bidDto, TestsVars.ID.getTestId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(Messages.NOT_FOUND_MESSAGE.getMessage());
        verify(bidRepo, never()).save(any());
    }
}
