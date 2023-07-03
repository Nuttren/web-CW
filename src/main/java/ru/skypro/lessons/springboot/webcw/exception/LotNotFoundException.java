package ru.skypro.lessons.springboot.webcw.exception;

public class LotNotFoundException extends RuntimeException{
    public LotNotFoundException(String message) {
        super(message);
    }
}
