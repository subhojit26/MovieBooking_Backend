package com.example.movieBookingApplication.DTO;

import lombok.Data;

@Data
public class TheatreDTO {
    private String theatreName;
    private String theatreLocation;
    private Integer theatreCapacity;
    private String theatreScreenType;
}
