package com.dollartrading.trading.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private Account account;
    private String currency;
    private Double bidValue;
    private Boolean isActive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return id.equals(bid.id) && currency.equals(bid.currency) && bidValue.equals(bid.bidValue) && isActive.equals(bid.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency, bidValue, isActive);
    }
}
