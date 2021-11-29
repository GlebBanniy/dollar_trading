package com.dollartrading.trading.service;

import com.dollartrading.trading.dto.BidDto;
import com.dollartrading.trading.dto.OperationStatusDto;
import com.dollartrading.trading.exceptions.EntityAddingException;
import com.dollartrading.trading.exceptions.EntityNotFoundException;
import com.dollartrading.trading.exceptions.EntityUpdatingException;
import com.dollartrading.trading.models.Bid;
import com.dollartrading.trading.repos.AccountRepo;
import com.dollartrading.trading.repos.BidRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class BidService {

    private final BidRepo bidRepo;
    private final AccountRepo accountRepo;

    @Autowired
    public BidService(BidRepo bidRepo, AccountRepo accountRepo) {
        this.bidRepo = bidRepo;
        this.accountRepo = accountRepo;
    }

    private Bid dtoToEntity(BidDto bidDto) {
        return Bid.builder()
                .bidValue(bidDto.getBidValue())
                .currency(bidDto.getCurrency())
                .account(accountRepo.findAccountByUsername(bidDto.getUserName()))
                .isActive(bidDto.getIsActive())
                .build();
    }

    public OperationStatusDto addBid(BidDto bidDto) throws EntityAddingException {
        try {
            return generateUpdatingMessage(bidRepo.save(dtoToEntity(bidDto)), Messages.ADDED_MESSAGE);
        } catch (Exception e) {
            log.error(Messages.ERROR_SAVING_ENTITY.getMessage(), e);
            throw new EntityAddingException(e);
        }
    }

    public OperationStatusDto updateBid(BidDto bidDto, Long id) throws EntityNotFoundException, EntityUpdatingException {
        Optional<Bid> bidFromDb = bidRepo.findById(id);
        Bid updatingBid = updateBid(getOldBid(bidFromDb), bidDto);
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
            throw new EntityUpdatingException(e);
        }
    }
}
