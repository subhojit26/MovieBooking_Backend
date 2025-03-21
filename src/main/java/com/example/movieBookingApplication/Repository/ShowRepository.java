package com.example.movieBookingApplication.Repository;

import com.example.movieBookingApplication.Entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show,Long> {
    Optional<List<Show>> findByMovieId(Long movieId);
    Optional<List<Show>> findByTheatreId(Long theatreId);
}
