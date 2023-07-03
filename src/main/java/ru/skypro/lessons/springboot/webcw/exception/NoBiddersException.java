package ru.skypro.lessons.springboot.webcw.exception;

public class NoBiddersException extends RuntimeException{
    public NoBiddersException(String message) {
        super(message);
    }
}
