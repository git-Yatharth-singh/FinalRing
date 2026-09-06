package com.example.FinalRing.Exception;

public class JwtInvalid extends RuntimeException{
    public JwtInvalid(String message){
        super(message);
    }
}
