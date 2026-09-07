package com.example.FinalRing.Exception;

public class PlayerAlreadyJoined extends RuntimeException{
    public PlayerAlreadyJoined(String message){
        super(message);
    }
}
