package com.example.movieBookingApplication.Service;

import com.example.movieBookingApplication.DTO.ShowDTO;
import com.example.movieBookingApplication.Entity.Booking;
import com.example.movieBookingApplication.Entity.Movie;
import com.example.movieBookingApplication.Entity.Show;
import com.example.movieBookingApplication.Entity.Theatre;
import com.example.movieBookingApplication.Repository.MovieRepository;
import com.example.movieBookingApplication.Repository.ShowRepository;
import com.example.movieBookingApplication.Repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    public Show createShow(ShowDTO showDTO){
        Movie movie=movieRepository.findById(showDTO.getMovieId())
                .orElseThrow(()->new RuntimeException("No movie found of id "+showDTO.getMovieId()));

        Theatre theatre=theatreRepository.findById(showDTO.getTheatreId())
                .orElseThrow(()->new RuntimeException("No theatre found of id "+ showDTO.getTheatreId()));

        Show show=new Show();
        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setTheatre(theatre);
        show.setMovie(movie);

        return showRepository.save(show);
    }

    public List<Show> getAllShows(){
        return showRepository.findAll();
    }

    public List<Show> getShowsByMovie(Long movieId){
        Optional<List<Show>> showListBox=showRepository.findByMovieId(movieId);

        if(showListBox.isPresent()){
            return showListBox.get();
        }else{
            throw new RuntimeException("No shows available for the movie "+movieId);
        }
    }

    public List<Show> getShowsByTheatre(Long theatreId){
        Optional<List<Show>> showListBox=showRepository.findByTheatreId(theatreId);

        if(showListBox.isPresent()){
            return showListBox.get();
        }else{
            throw new RuntimeException("No shows available for the theatre "+theatreId);
        }
    }

    public Show updateShow(Long id, ShowDTO showDTO){
        Show show=showRepository.findById(id)
                .orElseThrow(()->new RuntimeException("No show available for the id "+id));

        Movie movie=movieRepository.findById(showDTO.getMovieId())
                .orElseThrow(()->new RuntimeException("No movie found of id "+showDTO.getMovieId()));

        Theatre theatre=theatreRepository.findById(showDTO.getTheatreId())
                .orElseThrow(()->new RuntimeException("No theatre found of id "+ showDTO.getTheatreId()));

        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setTheatre(theatre);
        show.setMovie(movie);

        return showRepository.save(show);
    }

    public void deleteShow(Long id){
        if(!showRepository.existsById(id)){
            throw new RuntimeException("No show available for the id "+id);
        }

        List<Booking> bookings = showRepository.findById(id).get().getBookings();
        if(!bookings.isEmpty()){
            throw new RuntimeException("Can not delete show with existing bookings");
        }
        showRepository.deleteById(id);
    }


}
