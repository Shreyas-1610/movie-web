package com.movies.movieApi.exceptions;

public class EmptyFileException extends Throwable {
    public EmptyFileException(String message){
        super(message);
    }
}
