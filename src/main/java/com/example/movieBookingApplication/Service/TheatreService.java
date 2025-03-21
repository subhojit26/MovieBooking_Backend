package com.example.movieBookingApplication.Service;

import com.example.movieBookingApplication.DTO.TheatreDTO;
import com.example.movieBookingApplication.Entity.Theatre;
import com.example.movieBookingApplication.Repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheatreService {
    @Autowired
    private TheatreRepository theatreRepository;

    public Theatre addTheatre(TheatreDTO theatreDTO){
        Theatre theatre = new Theatre();

        theatre.setTheatreCapacity(theatreDTO.getTheatreCapacity());
        theatre.setTheatreLocation(theatreDTO.getTheatreLocation());
        theatre.setTheatreName(theatreDTO.getTheatreName());
        theatre.setTheatreScreenType(theatreDTO.getTheatreScreenType());

        return theatreRepository.save(theatre);
    }

    public List<Theatre> getTheatreByLocation(String location){
        Optional<List<Theatre>> theatreListBox= theatreRepository.findByLocation(location);

        if(theatreListBox.isPresent()){
            return theatreListBox.get();
        }else{
            throw new RuntimeException("No theatre found in location "+location);
        }
    }

    public Theatre updateTheatre(Long id,TheatreDTO theatreDTO){
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(()->new RuntimeException("No theatre found for the id "+id));

        theatre.setTheatreCapacity(theatreDTO.getTheatreCapacity());
        theatre.setTheatreLocation(theatreDTO.getTheatreLocation());
        theatre.setTheatreName(theatreDTO.getTheatreName());
        theatre.setTheatreScreenType(theatreDTO.getTheatreScreenType());
        return theatreRepository.save(theatre);
    }

    public void deleteTheatre(Long id){
        theatreRepository.deleteById(id);
    }
}
