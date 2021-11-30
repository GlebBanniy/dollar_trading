package com.dollartrading.trading.service;

public enum TestsVars {
    ID(1L),
    NAME("Gleb"),
    BANK_NAME("vtb"),
    CURRENCY("dollar"),
    PASSWORD("123"),
    BID_VALUE(5.55);

    String string;
    Long testId;
    Double value;

    TestsVars(String s) {
        this.string = s;
    }

    TestsVars(long l) {
        this.testId = l;
    }

    TestsVars(double d) {
        this.value = d;
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
}
