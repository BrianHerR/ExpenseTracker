package com.app.ExpenseTracker.exception;

import lombok.Getter;
import lombok.Setter;



public class UserNotFoundException extends Exception {

    private final String message ;

    public UserNotFoundException() {
        this.message = "No se encontro un usuario con ese id";
    }

    public UserNotFoundException (String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}


