package com.dollartrading.trading.repos;

import com.dollartrading.trading.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findAccountByUsername(String username);
    List<Account> findAccountsByPaymentName(String bankName);
}
