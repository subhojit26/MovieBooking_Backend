package com.example.movieBookingApplication.Repository;

import com.example.movieBookingApplication.Entity.Booking;
import com.example.movieBookingApplication.Entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByShowId(Long showId);
    List<Booking> findByStatus(BookingStatus bookingStatus);
}
