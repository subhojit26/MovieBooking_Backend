package com.example.movieBookingApplication.Repository;

import com.example.movieBookingApplication.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie,Long> {

    Optional<List<Movie>> findByGenre(String genre);
    Optional<List<Movie>> findByLanguage(String language);
    Optional<Movie> findByName(String name);
}
