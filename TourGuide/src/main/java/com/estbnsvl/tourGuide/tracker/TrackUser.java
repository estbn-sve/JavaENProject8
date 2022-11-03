package com.estbnsvl.tourGuide.tracker;

import com.estbnsvl.tourGuide.model.user.User;
import com.estbnsvl.tourGuide.service.TourGuideService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackUser extends Thread {

    private TourGuideService tourGuideService;
    private User user;

    public TrackUser(TourGuideService tourGuideService,User user){
        this.tourGuideService = tourGuideService;
        this.user = user;
    }

    @Override
    public void run() {
        log.info("Track started for the user : {}", user.getUserName());
        tourGuideService.trackUserLocation(user);
        log.info("Track ended for the user : {}", user.getUserName());
    }
}
