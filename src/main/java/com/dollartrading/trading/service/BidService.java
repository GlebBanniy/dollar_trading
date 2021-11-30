package com.dollartrading.trading.service;

import com.dollartrading.trading.dto.BidDto;
import com.dollartrading.trading.dto.OperationStatusDto;
import com.dollartrading.trading.exceptions.EntityAddingException;
import com.dollartrading.trading.exceptions.EntityNotFoundException;
import com.dollartrading.trading.exceptions.EntityUpdatingException;
import com.dollartrading.trading.models.Bid;
import com.dollartrading.trading.remote.CurrencyLayerClient;
import com.dollartrading.trading.repos.AccountRepo;
import com.dollartrading.trading.repos.BidRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@PropertySource("classpath:application.properties")
public class BidService {

    private final BidRepo bidRepo;
    private final AccountRepo accountRepo;
    private final CurrencyLayerClient currencyLayerClient;

    private final String key;
    private final String currencies;
    private final Integer format;

    @Autowired
    public BidService(BidRepo bidRepo, AccountRepo accountRepo, CurrencyLayerClient currencyLayerClient,
                      @Value("${url.access.key}") String key,
                      @Value("${url.currencies}") String currencies,
                      @Value("${url.format}") Integer format) {
        this.bidRepo = bidRepo;
        this.accountRepo = accountRepo;
        this.currencyLayerClient = currencyLayerClient;
        this.key = key;
        this.currencies = currencies;
        this.format = format;
    }

    private Bid dtoToEntity(BidDto bidDto) {
        return Bid.builder()
                .bidValue(bidDto.getBidValue())
                .currency(bidDto.getCurrency())
                .account(accountRepo.findAccountByUsername(bidDto.getUserName()))
                .isActive(bidDto.getIsActive())
                .build();
    }

    private double getActualChange(String key, String currencies, Integer format, String currency){
        return currencyLayerClient.getData(key, currencies, format).getQuotes().get(currency);
    }

    public OperationStatusDto addBid(BidDto bidDto) throws EntityAddingException {
        double actualChangeValue = getActualChange(key, currencies, format, bidDto.getCurrency());
        if (!(actualChangeValue-5 < bidDto.getBidValue() && actualChangeValue+5 > bidDto.getBidValue())){
            log.error(Messages.INCORRECT_BID_VALUE_MESSAGE.getMessage());
            throw new EntityAddingException(Messages.INCORRECT_BID_VALUE_MESSAGE.getMessage());
        }
        try {
            return generateUpdatingMessage(dtoToEntity(bidDto), Messages.ADDED_MESSAGE);
        } catch (Exception e) {
            log.error(Messages.ERROR_SAVING_ENTITY.getMessage(), e);
            throw new EntityAddingException(Messages.ERROR_SAVING_ENTITY.getMessage());
        }
    }

    public OperationStatusDto updateBid(BidDto bidDto, Long id) throws EntityNotFoundException, EntityUpdatingException {
        var bidFromDb = bidRepo.findById(id);
        var updatingBid = updateBid(getOldBid(bidFromDb), bidDto);
        return generateUpdatingMessage(updatingBid, Messages.UPDATED_MESSAGE);
    }

    public void deleteBid (Long id) {
        bidRepo.deleteById(id);
    }

    private Bid getOldBid(Optional<Bid> bidFromDb) throws EntityNotFoundException {
        if (bidFromDb.isEmpty()) {
            log.error(Messages.NOT_FOUND_MESSAGE.getMessage());
            throw new EntityNotFoundException(Messages.NOT_FOUND_MESSAGE.getMessage());
        }
        return bidFromDb.get();
    }

    private Bid updateBid(Bid oldBid, BidDto bidDto){
        oldBid.setBidValue(bidDto.getBidValue());
        oldBid.setCurrency(bidDto.getCurrency());
        oldBid.setIsActive(bidDto.getIsActive());
        oldBid.setAccount(accountRepo.findAccountByUsername(bidDto.getUserName()));
        return oldBid;
    }

    private OperationStatusDto generateUpdatingMessage(Bid updatingBid, Messages message) throws EntityUpdatingException {
        try {
            return OperationStatusDto.builder()
                    .id(bidRepo.save(updatingBid).getId())
                    .message(message.getMessage())
                    .build();
        } catch (Exception e) {
            log.error(Messages.ERROR_SAVING_ENTITY.getMessage(), e);
            throw new EntityUpdatingException(Messages.ERROR_SAVING_ENTITY.getMessage());
        }
    }
}
