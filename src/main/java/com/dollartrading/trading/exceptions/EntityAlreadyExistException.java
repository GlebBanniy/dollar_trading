package com.dollartrading.trading.exceptions;

public class EntityAlreadyExistException extends Exception{
    public EntityAlreadyExistException(String message){
        super(message);
    }
}
