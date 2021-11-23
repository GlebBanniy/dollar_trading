package com.dollartrading.trading.repos;

import com.dollartrading.trading.models.Account;
import com.dollartrading.trading.models.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepo extends JpaRepository<Bid, Long> {
    List<Bid> findBidsByCurrency(String currency);
    List<Bid> findBidsByUser(Account user);
    List<Bid> findBidsByBidValue(Double value);
}
