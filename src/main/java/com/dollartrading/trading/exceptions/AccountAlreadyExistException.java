package com.dollartrading.trading.exceptions;

public class AccountAlreadyExistException extends Exception{
    public AccountAlreadyExistException(String message){
        super(message);
    }
}
