package com.bvan.movietheater.repository;

import com.bvan.movietheater.entity.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {
}
