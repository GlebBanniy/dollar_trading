package com.dollartrading.trading.service;

import com.dollartrading.trading.dto.BidDto;
import com.dollartrading.trading.dto.CurrencyLayerDto;
import com.dollartrading.trading.dto.OperationStatusDto;
import com.dollartrading.trading.exceptions.EntityAddingException;
import com.dollartrading.trading.exceptions.EntityNotFoundException;
import com.dollartrading.trading.exceptions.EntityUpdatingException;
import com.dollartrading.trading.models.Account;
import com.dollartrading.trading.models.Bid;
import com.dollartrading.trading.remote.CurrencyLayerClient;
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
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.dollartrading.trading.service.TestsVars.BANK_NAME;
import static com.dollartrading.trading.service.TestsVars.CORRECT_BID_VALUE;
import static com.dollartrading.trading.service.TestsVars.ID;
import static com.dollartrading.trading.service.TestsVars.NAME;
import static com.dollartrading.trading.service.TestsVars.PASSWORD;
import static com.dollartrading.trading.service.TestsVars.PRIVACY;
import static com.dollartrading.trading.service.TestsVars.TERMS;
import static com.dollartrading.trading.service.TestsVars.TIMESTAMP;
import static com.dollartrading.trading.service.TestsVars.TRUE_VALUE;
import static com.dollartrading.trading.service.TestsVars.USD;
import static com.dollartrading.trading.service.TestsVars.USDEUR;
import static com.dollartrading.trading.service.TestsVars.USDRUB;
import static com.dollartrading.trading.service.TestsVars.USDUSD;
import static com.dollartrading.trading.service.TestsVars.USD_EUR_BID_VALUE;
import static com.dollartrading.trading.service.TestsVars.USD_RUB_BID_VALUE;
import static com.dollartrading.trading.service.TestsVars.USD_RUB_INCORRECT_BID_VALUE;
import static com.dollartrading.trading.service.TestsVars.USD_USD_BID_VALUE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@PropertySource("classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock
    private BidRepo bidRepo;

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private CurrencyLayerClient currencyLayerClient;

    private CurrencyLayerDto currencyLayerDto;
    private CurrencyLayerDto incorrectCurrencyLayerDto;
    private AutoCloseable autoCloseable;
    private BidService bidService;
    private Bid bidFromDb;
    private Bid bid;
    private BidDto bidDto;
    private Account accountFromDb;

    private String key;
    private String currencies;
    private Integer format;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        key = "test";
        currencies = "test";
        format = 1;
        bidService = new BidService(bidRepo, accountRepo, currencyLayerClient, key, currencies, format);
        accountFromDb = Account.builder()
                .id(ID.getTestId())
                .password(PASSWORD.getString())
                .paymentName(BANK_NAME.getString())
                .username(NAME.getString())
                .build();
        bidFromDb = Bid.builder()
                .id(ID.getTestId())
                .bidValue(CORRECT_BID_VALUE.getValue())
                .currency(USDRUB.getString())
                .account(accountFromDb)
                .isActive(TRUE_VALUE.isbValue())
                .build();
        bid = Bid.builder()
                .bidValue(CORRECT_BID_VALUE.getValue())
                .currency(USDRUB.getString())
                .account(accountFromDb)
                .isActive(TRUE_VALUE.isbValue())
                .build();
        bidDto = new BidDto(
                NAME.getString(),
                USDRUB.getString(),
                CORRECT_BID_VALUE.getValue(),
                TRUE_VALUE.isbValue());
        Map<String, Double> map = new HashMap<>();
        map.put(USDUSD.getString(), USD_USD_BID_VALUE.getValue());
        map.put(USDEUR.getString(), USD_EUR_BID_VALUE.getValue());
        map.put(USDRUB.getString(), USD_RUB_BID_VALUE.getValue());
        Map<String, Double> incorrectMap = new HashMap<>();
        incorrectMap.put(USDUSD.getString(), USD_USD_BID_VALUE.getValue());
        incorrectMap.put(USDEUR.getString(), USD_EUR_BID_VALUE.getValue());
        incorrectMap.put(USDRUB.getString(), USD_RUB_INCORRECT_BID_VALUE.getValue());
        currencyLayerDto = new CurrencyLayerDto(
                map,
                TRUE_VALUE.isbValue(),
                TERMS.getString(),
                PRIVACY.getString(),
                TIMESTAMP.getTestId(),
                USD.getString());
        incorrectCurrencyLayerDto = new CurrencyLayerDto(
                incorrectMap,
                TRUE_VALUE.isbValue(),
                TERMS.getString(),
                PRIVACY.getString(),
                TIMESTAMP.getTestId(),
                USD.getString());
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
        when(currencyLayerClient.getData(any(String.class), any(String.class), any(Integer.class))).thenReturn(currencyLayerDto);
        when(accountRepo.findAccountByUsername(any(String.class))).thenReturn(accountFromDb);
        when(bidRepo.save(any(Bid.class))).thenReturn(bidFromDb);
        var result = bidService.addBid(bidDto);

        var value = captureFinalVariable();
        assertThat(value).isEqualTo(bid);

        var rightValue = OperationStatusDto.builder()
                .id(ID.getTestId()).message(Messages.ADDED_MESSAGE.getMessage()).build();
        assertThat(result).isEqualTo(rightValue);
    }

    @Test
    void willThrowWhenEntityAlreadyExist() {
        when(currencyLayerClient.getData(key, currencies, format)).thenReturn(currencyLayerDto);
        given(bidRepo.save(bid))
                .willThrow(RuntimeException.class);
        assertThatThrownBy(() -> bidService.addBid(bidDto))
                .isInstanceOf(EntityAddingException.class)
                .hasMessageContaining(Messages.ERROR_SAVING_ENTITY.getMessage());
    }

    @Test
    void willThrowWhenBidValueIncorrect() {
        when(currencyLayerClient.getData(key, currencies, format)).thenReturn(incorrectCurrencyLayerDto);
        assertThatThrownBy(() -> bidService.addBid(bidDto))
                .isInstanceOf(EntityAddingException.class)
                .hasMessageContaining(Messages.INCORRECT_BID_VALUE_MESSAGE.getMessage());
    }

    @Test
    void tryUpdateBid() throws EntityUpdatingException, EntityNotFoundException {
        when(bidRepo.save(any(Bid.class))).thenReturn(bidFromDb);
        when(bidRepo.findById(any(Long.class))).thenReturn(Optional.ofNullable(bidFromDb));
        var result = bidService.updateBid(bidDto, ID.getTestId());

        var value = captureFinalVariable();
        assertThat(value).isEqualTo(bid);

        OperationStatusDto rightValue = OperationStatusDto.builder()
                .id(ID.getTestId()).message(Messages.UPDATED_MESSAGE.getMessage()).build();
        assertThat(result).isEqualTo(rightValue);
    }

    @Test
    void willThrowWhenEntityNotFound() {
        Optional<Bid> bidOptional = Optional.empty();
        given(bidRepo.findById(ID.getTestId()))
                .willReturn(bidOptional);
        assertThatThrownBy(() -> bidService.updateBid(bidDto, ID.getTestId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(Messages.NOT_FOUND_MESSAGE.getMessage());
        verify(bidRepo, never()).save(any());
    }
}
