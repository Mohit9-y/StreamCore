package com.bingeForge.demo.service;

import com.bingeForge.demo.dto.MovieRequestDto;
import com.bingeForge.demo.dto.MovieResponseDto;
import com.bingeForge.demo.entity.Movie;
import com.bingeForge.demo.repository.MovieRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    public MovieService( MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    @Transactional
    public MovieResponseDto addMovie(MovieRequestDto movieRequestDto){
        Movie movie = reqToEntity(movieRequestDto);
        Movie savedMovie = movieRepository.save(movie);
        return entityToRes(savedMovie);
    }

    public Void deleteMovie(UUID movieId){
        Movie movie = movieRepository.findById(movieId)
                        .orElseThrow(()->new ResourceNotFoundException("Movie not found with id: "+movieId));

        movieRepository.deleteById(movieId);
    }

    private Movie reqToEntity(MovieRequestDto movieRequestDto){
        Movie movie = new Movie();

        movie.setVideoUrl(movieRequestDto.getVideoUrl());
        movie.setTitle(movieRequestDto.getTitle());
        movie.setDescription(movieRequestDto.getDescription());
        movie.setThumbnailUrl(movieRequestDto.getThumbnailUrl());
        movie.setTrailerUrl(movieRequestDto.getTrailerUrl());
        movie.setDurationInSeconds((movieRequestDto.getDurationInSeconds()));
        movie.setReleaseYear(movieRequestDto.getReleaseYear());
        movie.setCastMembers(movieRequestDto.getCastMembers());
        movie.setIsPremium(movieRequestDto.getIsPremium());
        movie.setRentAmount(movieRequestDto.getRentAmount());

        return movie;
    }

    private MovieResponseDto entityToRes(Movie movie){
        MovieResponseDto movieResponseDto = new MovieResponseDto();

        movieResponseDto.setId(movie.getId());
        movieResponseDto.setVideoUrl(movie.getVideoUrl());
        movieResponseDto.setTitle(movie.getTitle());
        movieResponseDto.setDescription(movie.getDescription());
        movieResponseDto.setThumbnailUrl(movie.getThumbnailUrl());
        movieResponseDto.setTrailerUrl(movie.getTrailerUrl());
        movieResponseDto.setDurationInSeconds(movie.getDurationInSeconds());
        movieResponseDto.setCastMembers(movie.getCastMembers());
        movieResponseDto.setReleaseYear(movie.getReleaseYear());
        movieResponseDto.setIsPremium(movie.getIsPremium());
        movieResponseDto.setRentAmount(movie.getRentAmount());
        movieResponseDto.setCreatedAt(movie.getCreatedAt());

        return movieResponseDto;
    }
}
