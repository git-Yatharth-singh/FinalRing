package com.example.FinalRing.Exception;

public class PlayerNotInMatchException extends RuntimeException{
    public PlayerNotInMatchException(String message){
        super(message);
    }
}
