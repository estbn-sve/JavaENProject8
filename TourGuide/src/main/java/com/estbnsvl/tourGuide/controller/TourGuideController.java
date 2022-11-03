package com.estbnsvl.tourGuide.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import com.estbnsvl.tourGuide.service.TourGuideService;
import com.estbnsvl.tourGuide.model.user.User;
import tripPricer.Provider;



@RestController
@RequestMapping("")
@Slf4j
public class TourGuideController {

	@Autowired
	private TourGuideService tourGuideService;
	
    @RequestMapping("/")
    public String index() {
        log.info("GET /");
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        log.info("GET /getLocation");
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }
    
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
//    @RequestMapping("/getNearbyAttractions")
//    public String getNearbyAttractions(@RequestParam String userName) {
//        log.info("GET /getNearbyAttractions");
//    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
//    	return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
//    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
        log.info("GET /getRewards");
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }
    
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        log.info("GET /getAllCurrentLocations");
    	// TODO: Get a list of every user's most recent location as JSON
    	//- Note: does not use gpsUtil to query for their current location, 
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371} 
    	//        ...
    	//     }
    	
    	return JsonStream.serialize("");
    }
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        log.info("GET /getTripDeals");
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
    	return JsonStream.serialize(providers);
    }
    
    private User getUser(String userName) {
        log.info("GET /");
        return tourGuideService.getUser(userName);
    }
   

}