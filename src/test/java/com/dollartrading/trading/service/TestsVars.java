package com.dollartrading.trading.service;

public enum TestsVars {

    ID(1L),
    TIMESTAMP(1639037404L),
    NAME("Gleb"),
    NAME_2("Vasia"),
    NAME_3("Ivan"),
    TOO_LONG_NAME("VeryLongAndCoolName"),
    BANK_NAME("vtb"),
    USDRUB("USDRUB"),
    USDUSD("USDUSD"),
    USDEUR("USDEUR"),
    TERMS("https://currencylayer.com/terms"),
    PRIVACY("https://currencylayer.com/privacy"),
    USD("USD"),
    PASSWORD("123"),
    CORRECT_BID_VALUE(73.55),
    USD_USD_BID_VALUE(1.0),
    USD_RUB_BID_VALUE(73.6),
    USD_EUR_BID_VALUE(6.55),
    USD_RUB_INCORRECT_BID_VALUE(66.55),
    TRUE_VALUE(true);

    String string;
    Long testId;
    Double value;
    boolean bValue;

    TestsVars(String s) {
        this.string = s;
    }

    TestsVars(long l) {
        this.testId = l;
    }

    TestsVars(double d) {
        this.value = d;
    }

    TestsVars(boolean b) {
        this.bValue = b;
    }

    public String getString() {
        return string;
    }

    public Long getTestId() {
        return testId;
    }

    public Double getValue() {
        return value;
    }

    public boolean isbValue() {
        return bValue;
    }
}
