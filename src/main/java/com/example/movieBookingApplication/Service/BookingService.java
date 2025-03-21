package com.example.movieBookingApplication.Service;

import com.example.movieBookingApplication.DTO.BookingDTO;
import com.example.movieBookingApplication.Entity.Booking;
import com.example.movieBookingApplication.Entity.BookingStatus;
import com.example.movieBookingApplication.Entity.Show;
import com.example.movieBookingApplication.Entity.User;
import com.example.movieBookingApplication.Repository.BookingRepository;
import com.example.movieBookingApplication.Repository.ShowRepository;
import com.example.movieBookingApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository

    public Booking createBooking(BookingDTO bookingDTO){
        Show show=showRepository.findById(bookingDTO.getShowId())
                .orElseThrow(()->new RuntimeException("Show not found"));

        if(!isSeatsAvailable(show.getId(), bookingDTO.getNumberOfSeats())){
            throw new RuntimeException("Seats are not available");
        }
        if(bookingDTO.getSeatNumbers().size()!=bookingDTO.getNumberOfSeats()){
            throw new RuntimeException("Seat numbers and Number of seats must be equal");
        }
        validateDuplicateSeats(show.getId(), bookingDTO.getSeatNumbers());

        User user=userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(()->new RuntimeException("No user found"));

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setShow(show);
        booking.setNumberOfSeats(bookingDTO.getNumberOfSeats());
        booking.setPrice(calculateTotalAmount(show.getPrice(),bookingDTO.getNumberOfSeats()));
        booking.setSeatNumbers(bookingDTO.getSeatNumbers());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setBookingTime(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId){
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getShowBookings(Long showId){
        return bookingRepository.findByShowId(showId);
    }

    public Booking confirmBooking(long bookingId){
        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->new RuntimeException("No show found"));

        if(booking.getBookingStatus()!=BookingStatus.PENDING){
            throw new RuntimeException("Booking is not in pending stage");
        }
        booking.setBookingStatus(BookingStatus.COMFIRMED);
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long bookingId){
        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->new RuntimeException("No show found"));

        validateCancellation(booking);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingByStatus(BookingStatus bookingStatus){
        return bookingRepository.findByStatus(bookingStatus);
    }

    public void validateCancellation(Booking booking){
        LocalDateTime showTime=booking.getBookingTime();
        LocalDateTime deadlineTime=showTime.minusHours(2);

        if(LocalDateTime.now().isAfter(deadlineTime)){
            throw new RuntimeException("Cannot cancel booking");
        }
        if(booking.getBookingStatus()==BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }
    }


    public Boolean isSeatsAvailable(Long showId, Integer numberOfSeats){
        Show show=showRepository.findById(showId)
                .orElseThrow(()->new RuntimeException("Show not found"));

        int bookedSeats=show.getBookings().stream()
                .filter(booking->booking.getBookingStatus()!= BookingStatus.CANCELLED)
                .mapToInt(Booking::getNumberOfSeats)
                .sum();
        return (show.getTheatre().getTheatreCapacity()-bookedSeats)>=numberOfSeats;
    }

    public void validateDuplicateSeats(Long showId, List<String> seatNumbers){
        Show show=showRepository.findById(showId)
                .orElseThrow(()->new RuntimeException("Show not found"));

        Set<String> occupiedSeats=show.getBookings().stream()
                .filter(b->b.getBookingStatus()!=BookingStatus.CANCELLED)
                .flatMap(b->b.getSeatNumbers().stream())
                .collect(Collectors.toSet());

        List<String> duplicateSeats=seatNumbers.stream()
                .filter(occupiedSeats::contains)
                .toList();

        if(!duplicateSeats.isEmpty()){
            throw new RuntimeException("Seats are already booked");
        }
    }

    public Double calculateTotalAmount(Double price, Integer numberOfSeats){
        return price*numberOfSeats;
    }

}
