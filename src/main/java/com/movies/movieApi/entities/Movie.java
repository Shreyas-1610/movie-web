/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.movies.movieApi.entities;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable=false, length=200)
    @NotBlank(message="Provide movie title")
    private String title;

    @Column(nullable=false, length=200)
    @NotBlank(message="Provide director's director")
    private String director;

    @Column(nullable=false, length=200)
    @NotBlank(message="Provide director's studio")
    private String studio;

    @ElementCollection
    @CollectionTable(name="movie_cast")
    private Set<String> movieCast;

    @Column(nullable=false)
    private Integer releaseYear;

    @Column(nullable=false)
    @NotBlank(message="Provide movie's poster")
    private String poster;

}
