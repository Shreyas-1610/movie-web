package com.movies.movieApi.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movies.movieApi.dto.MovieDto;
import com.movies.movieApi.dto.MoviePageResponse;
import com.movies.movieApi.entities.Movie;
import com.movies.movieApi.exceptions.FileExistsException;
import com.movies.movieApi.exceptions.MovieNotFoundException;
import com.movies.movieApi.repositories.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;
    
    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // upload the file
       if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
        throw new FileExistsException("File already exists with same name. Please enter another name");
       }
        String uploadedFileName = fileService.uploadFile(path, file);

        //set the value of field 'poster' as filename
        movieDto.setPoster(uploadedFileName);

        //map dto to movie object
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //save the movie object 
        Movie savedMovie = movieRepository.save(movie);

        //generate the poster url
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        //map movie to dto and return
        MovieDto response = new MovieDto(
            savedMovie.getMovieId(),
            savedMovie.getTitle(),
            savedMovie.getDirector(),
            savedMovie.getStudio(),
            savedMovie.getMovieCast(),
            savedMovie.getReleaseYear(),
            savedMovie.getPoster(),
            posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        //check in db and fetch
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: "+movieId));

        //generate posterUrl
        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        //map to movieDto obj and return
        MovieDto response = new MovieDto(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterUrl
        );

        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        //fetch all from db
        List<Movie> movies = movieRepository.findAll();

        List<MovieDto> movieDtos = new ArrayList<>();
        //iterate through the list & nerate url for each movie and map to movieobj
        for(Movie movie : movies){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterUrl
        );
            movieDtos.add(movieDto);
        }
        return movieDtos;
    }



    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        //check if movie already exists
        Movie mv = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: "+movieId));
        
        //if fole is null, do nothing if file is not null delete the old and upload new
        String fileName = mv.getPoster();
        if(file != null){
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }
        //set movieDtos poster value
        movieDto.setPoster(fileName);

        //map it to movie object
        Movie movie = new Movie(
            mv.getMovieId(),
            movieDto.getTitle(),
            movieDto.getDirector(),
            movieDto.getStudio(),
            movieDto.getMovieCast(),
            movieDto.getReleaseYear(),
            movieDto.getPoster()
        );

        //save the movie obj and save movie obj
        Movie updatedMovie = movieRepository.save(movie);

        //generate poster url
        String posterUrl = baseUrl + "/file/" + fileName;

        MovieDto response = new MovieDto(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterUrl
        );
        

        return response;
    }



    @Override
    public String deleteMovie(Integer movieId) throws IOException {
       //check if movie exist
       Movie mv = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: "+movieId));
       Integer id = mv.getMovieId();
       //delete the file
       Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));

       //delete the movie obj
       movieRepository.delete(mv);
       return "Movie deleted with id: "+id;
    }



    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
        //iterate through the list & nerate url for each movie and map to movieobj
        for(Movie movie : movies){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterUrl
        );
            movieDtos.add(movieDto);
        }
        return new MoviePageResponse(movieDtos, pageNumber, pageSize, moviePages.getTotalElements(), moviePages.getTotalPages(), moviePages.isLast());
    }



    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy,
            String dir) {
        Sort sort = dir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending()
                                :Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
                        
        List<MovieDto> movieDtos = new ArrayList<>();
        //iterate through the list & nerate url for each movie and map to movieobj
            for(Movie movie : movies){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterUrl
        );
            movieDtos.add(movieDto);
        }
    return new MoviePageResponse(movieDtos, pageNumber, pageSize, moviePages.getTotalElements(), moviePages.getTotalPages(), moviePages.isLast());
    }
}
