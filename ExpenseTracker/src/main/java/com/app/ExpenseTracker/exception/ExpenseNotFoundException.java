package com.app.ExpenseTracker.exception;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException(String message){
        super(message);
    }
}
