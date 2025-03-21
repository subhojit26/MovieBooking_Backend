package com.example.movieBookingApplication.Service;

import com.example.movieBookingApplication.DTO.MovieDTO;
import com.example.movieBookingApplication.Entity.Movie;
import com.example.movieBookingApplication.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private static MovieRepository movieRepository;

    public static Movie addMovie(MovieDTO movieDTO){
        Movie movie=new Movie();
        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setDuration(movieDTO.getDuration());
        movie.setGenre(movieDTO.getGenre());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setLanguage(movieDTO.getLanguage());
        return movieRepository.save(movie);
    }

    public static List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public static List<Movie> getMoviesByGenre(String genre){
        Optional<List<Movie>> listOfMovieBox= movieRepository.findByGenre(genre);
        if(listOfMovieBox.isPresent()){
            return listOfMovieBox.get();
        }else{
            throw new RuntimeException("No movies found for the genre "+ genre);
        }
    }

    public static List<Movie> getMoviesByLanguage(String language){
        Optional<List<Movie>> listOfMovieBox= movieRepository.findByLanguage(language);
        if(listOfMovieBox.isPresent()){
            return listOfMovieBox.get();
        }else{
            throw new RuntimeException("No movies found for the language "+ language);
        }
    }

    public static Movie getMovieByTitle(String name){
        Optional<Movie> movieBox= movieRepository.findByName(name);
        if(movieBox.isPresent()){
            return movieBox.get();
        }else{
            throw new RuntimeException("No movie found for the title "+name);
        }
    }

    public static Movie updateMovie(Long id, MovieDTO movieDTO){
        Movie movie=movieRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No movie found for the id "+id));

        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setDuration(movieDTO.getDuration());
        movie.setGenre(movieDTO.getGenre());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setLanguage(movieDTO.getLanguage());
        return movieRepository.save(movie);
    }

    public static void deleteMovie(Long id){
        movieRepository.deleteById(id);
    }
}
