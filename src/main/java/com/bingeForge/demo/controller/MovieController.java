package com.bingeForge.demo.controller;

import com.bingeForge.demo.dto.movie.MovieRequestDto;
import com.bingeForge.demo.dto.movie.MovieResponseDto;
import com.bingeForge.demo.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity addMovie(@Valid @RequestBody MovieRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.addMovie(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity getMovie(@PathVariable UUID id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping
    public ResponseEntity getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}