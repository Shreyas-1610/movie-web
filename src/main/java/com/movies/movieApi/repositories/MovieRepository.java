/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.movies.movieApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movies.movieApi.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
