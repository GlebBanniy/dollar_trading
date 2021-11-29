package com.dollartrading.trading.controllers;

import com.dollartrading.trading.dto.BidDto;
import com.dollartrading.trading.dto.OperationStatusDto;
import com.dollartrading.trading.exceptions.EntityAddingException;
import com.dollartrading.trading.exceptions.EntityNotFoundException;
import com.dollartrading.trading.exceptions.EntityUpdatingException;
import com.dollartrading.trading.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bid")
public class BidController {
    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationStatusDto> addBid(
            @RequestBody BidDto bidDto) throws EntityAddingException {
        return new ResponseEntity<>(bidService.addBid(bidDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationStatusDto> updateBid(
            @RequestBody BidDto bidDto,
            @PathVariable("id") Long bidId) throws EntityUpdatingException, EntityNotFoundException {
        return new ResponseEntity<>(bidService.updateBid(bidDto, bidId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBid(@PathVariable("id") Long bidId) {
        bidService.deleteBid(bidId);
    }
}
