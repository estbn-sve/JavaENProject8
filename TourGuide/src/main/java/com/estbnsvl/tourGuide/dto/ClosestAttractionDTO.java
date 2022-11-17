package com.estbnsvl.tourGuide.dto;

import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClosestAttractionDTO {

    private String name;
    private Location location;
    private Location userLocation;
    private Double distanceBetweenUserAndAttraction;
    private Integer rewardPoint;
}
