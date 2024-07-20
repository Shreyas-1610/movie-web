package com.movies.movieApi.dto;

import java.util.List;

public record MoviePageResponse(List<MovieDto> MovieDtos, Integer pageNumber, Integer pageSize,
                                long totalElements, int totalPages, boolean isLast) {
        
}
