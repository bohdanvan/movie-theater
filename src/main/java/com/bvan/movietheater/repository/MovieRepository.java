package com.bvan.movietheater.repository;

import com.bvan.movietheater.entity.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
}
