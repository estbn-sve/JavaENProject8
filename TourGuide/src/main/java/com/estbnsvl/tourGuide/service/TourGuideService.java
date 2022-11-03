package com.estbnsvl.tourGuide.service;

import java.util.*;
import com.estbnsvl.tourGuide.userapi.InternalUserApi;
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
		/*traitementMultiThread();
	}

	private void traitementMultiThread(){
		int userMax = InternalTestHelper.getInternalUserNumber();
		int userByThread = 50;
		double numberThread = (double) userMax / (double) userByThread;
		double numberThreadMax= Math.floor(numberThread);
		if (numberThread>numberThreadMax){
			numberThreadMax = numberThreadMax+1;
		}

		internalUserApi.runInitializeInternalUsers();
		List<User> userList = getAllUsers();
		List<User> userListOk = new ArrayList<>();
		for(int i=1; i<=numberThreadMax;i++) {
			int g = i;
			new Thread(() -> {
				for (int y = (g - 1) * 100; y < g * 100; y++) {
					userListOk.add(userList.get(y));
					trackUserLocation(userListOk.get(y));
					getTripDeals(userListOk.get(y));
					getNearByAttractions(trackUserLocation(userListOk.get(y)));
				}
			}).start();
		}*/
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
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

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();
		List<Provider> providers = tripPricer.getPrice(internalUserApi.getTripPricerApiKey(), user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
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