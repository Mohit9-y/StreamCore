package com.bingeForge.demo.service;

import com.bingeForge.demo.dto.movie.MovieRequestDto;
import com.bingeForge.demo.dto.movie.MovieResponseDto;
import com.bingeForge.demo.entity.Movie;
import com.bingeForge.demo.exception.ResourceNotFoundException;
import com.bingeForge.demo.mapper.MovieMapper;
import com.bingeForge.demo.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class  MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Transactional
    public MovieResponseDto addMovie(MovieRequestDto movieRequestDto) {
        // Assuming reqToEntity is in your mapper now
        Movie movie = movieMapper.reqToEntity(movieRequestDto);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.entityToRes(savedMovie);
    }

    public MovieResponseDto getMovieById(UUID movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        return movieMapper.entityToRes(movie);
    }

    public List getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::entityToRes)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMovie(UUID movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found with id: " + movieId);
        }
        movieRepository.deleteById(movieId);
    }
}