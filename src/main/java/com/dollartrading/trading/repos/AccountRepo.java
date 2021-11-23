package com.dollartrading.trading.repos;

import com.dollartrading.trading.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findAccountByUsername(String username);
    List<Account> findAccountsByPaymentName(String bankName);
}
