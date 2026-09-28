package com.bingeForge.demo.mapper;

import com.bingeForge.demo.dto.movie.MovieRequestDto;
import com.bingeForge.demo.dto.movie.MovieResponseDto;
import com.bingeForge.demo.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public Movie reqToEntity(MovieRequestDto movieRequestDto){
        Movie movie = new Movie();

        // Records use the field name as the accessor method, not "get"
        movie.setVideoUrl(movieRequestDto.videoUrl());
        movie.setTitle(movieRequestDto.title());
        movie.setDescription(movieRequestDto.description());
        movie.setThumbnailUrl(movieRequestDto.thumbnailUrl());
        movie.setTrailerUrl(movieRequestDto.trailerUrl());
        movie.setDurationInSeconds(movieRequestDto.durationInSeconds());
        movie.setReleaseYear(movieRequestDto.releaseYear());
        movie.setCastMembers(movieRequestDto.castMembers());
        movie.setIsPremium(movieRequestDto.isPremium());
        movie.setRentAmount(movieRequestDto.rentAmount());

        return movie;
    }

    public MovieResponseDto entityToRes(Movie movie){
        // Records are immutable, so we use the Lombok Builder to construct it
        return MovieResponseDto.builder()
                .id(movie.getId())
                .videoUrl(movie.getVideoUrl())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .thumbnailUrl(movie.getThumbnailUrl())
                .trailerUrl(movie.getTrailerUrl())
                .durationInSeconds(movie.getDurationInSeconds())
                .castMembers(movie.getCastMembers())
                .releaseYear(movie.getReleaseYear())
                .isPremium(movie.getIsPremium())
                .rentAmount(movie.getRentAmount())
                .createdAt(movie.getCreatedAt())
                .build();
    }
}