package com.estbnsvl.tourGuide.service;

import java.util.*;
import java.util.stream.Collectors;

import com.estbnsvl.tourGuide.dto.ClosestAttractionDTO;
import com.estbnsvl.tourGuide.dto.CurrentLocationDTO;
import com.estbnsvl.tourGuide.userapi.InternalUserApi;
import gpsUtil.location.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import com.estbnsvl.tourGuide.tracker.Tracker;
import com.estbnsvl.tourGuide.model.user.User;
import com.estbnsvl.tourGuide.model.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
@Slf4j
public class TourGuideService {

	@Autowired
	private Tracker tracker;

	@Autowired
	private TripPricer tripPricer;



	private GpsUtil gpsUtil;
	private RewardsService rewardsService;
	private InternalUserApi internalUserApi;



	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, InternalUserApi internalUserApi) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
		this.internalUserApi = internalUserApi;
	}

	public User getUser(String userName) {
		return internalUserApi.getInternalUserMap(userName);
	}

	public List<User> getAllUsers() {
		return new ArrayList<>(internalUserApi.getInternalUserMap().values());
		//return internalUserApi.internalUserMap.values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		internalUserApi.addUser(user);
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	public List<ClosestAttractionDTO> getClosestAttractions(User user) throws NullPointerException{
		return gpsUtil.getAttractions().stream()
				.map(attraction -> new ClosestAttractionDTO(
						attraction.attractionName,
						new Location(attraction.latitude,attraction.longitude),
						user.getLastVisitedLocation().location,
						rewardsService.getDistance(user.getLastVisitedLocation().location,new Location(attraction.latitude,attraction.longitude)),
						rewardsService.getRewardPoints(attraction, user)
				))
				.sorted(Comparator.comparing(ClosestAttractionDTO::getDistanceBetweenUserAndAttraction))
				.limit(5)
				.collect(Collectors.toList());
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public List<CurrentLocationDTO> getAllCurrentLocations() {
		return getAllUsers().stream()
				.map(user -> new CurrentLocationDTO(
						user.getUserId().toString(),
						user.getLastVisitedLocation().location
				))
				.collect(Collectors.toList());
	}

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();
		List<Provider> providers = tripPricer.getPrice(internalUserApi.getTripPricerApiKey(), user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for(Attraction attraction : gpsUtil.getAttractions()) {
			if(rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}

		return nearbyAttractions;
	}

	public void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
		      public void run() {
		        tracker.stopTracking();
		      }
		    });
	}
}