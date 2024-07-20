/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.movies.movieApi.exceptions;

/**
 *
 * @author Shreyas
 */
public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String message){
        super(message);
    }
}
