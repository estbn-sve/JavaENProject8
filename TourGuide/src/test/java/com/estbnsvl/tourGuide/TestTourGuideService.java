package com.estbnsvl.tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.estbnsvl.tourGuide.tracker.Tracker;
import com.estbnsvl.tourGuide.userapi.InternalUserApi;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.estbnsvl.tourGuide.helper.InternalTestHelper;
import com.estbnsvl.tourGuide.service.RewardsService;
import com.estbnsvl.tourGuide.service.TourGuideService;
import com.estbnsvl.tourGuide.model.user.User;
import tripPricer.Provider;

public class TestTourGuideService {

	//TODO Corriger les tests unitaires échouant par intermittence en raison d'une erreurConcurrentModificationException

	@Before
	public void setup(){
		Locale.setDefault(Locale.UK);
	}

	@Test
	public void getUserLocation() {
		InternalUserApi internalUserApi = new InternalUserApi();
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(10);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
		Tracker tracker = new Tracker(tourGuideService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@com.estbnsvl.tourGuide.com");
		System.out.println(tourGuideService);
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
	
	@Test
	public void addUser() {
		InternalUserApi internalUserApi = new InternalUserApi();
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
		Tracker tracker = new Tracker(tourGuideService);


		User user = new User(UUID.randomUUID(), "jon", "000", "jon@com.estbnsvl.tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@com.estbnsvl.tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tracker.stopTracking();
		
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}
	
	@Test
	public void getAllUsers() {
		InternalUserApi internalUserApi = new InternalUserApi();
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(100);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
		Tracker tracker = new Tracker(tourGuideService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@com.estbnsvl.tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@com.estbnsvl.tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();

		tracker.stopTracking();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	public void trackUser() {
		InternalUserApi internalUserApi = new InternalUserApi();
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
		Tracker tracker = new Tracker(tourGuideService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@com.estbnsvl.tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}
	
//	@Ignore // Not yet implemented
//	@Test
//	public void getNearbyAttractions() {
//		InternalUserApi internalUserApi = new InternalUserApi();
//		GpsUtil gpsUtil = new GpsUtil();
//		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
//		InternalTestHelper.setInternalUserNumber(0);
//		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
//		Tracker tracker = new Tracker(tourGuideService);
//
//		User user = new User(UUID.randomUUID(), "jon", "000", "jon@com.estbnsvl.tourGuide.com");
//		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
//
//		List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);
//
//		tracker.stopTracking();
//
//		assertEquals(5, attractions.size());
//	}

	@Test
	public void getTripDeals() {
		InternalUserApi internalUserApi = new InternalUserApi();
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
		Tracker tracker = new Tracker(tourGuideService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@com.estbnsvl.tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);
		
		tracker.stopTracking();
		
		assertEquals(10, providers.size());
	}
	
	
}
