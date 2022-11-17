package com.estbnsvl.tourGuide;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.estbnsvl.tourGuide.reward.Reward;
import com.estbnsvl.tourGuide.tracker.Tracker;
import com.estbnsvl.tourGuide.userapi.InternalUserApi;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.estbnsvl.tourGuide.helper.InternalTestHelper;
import com.estbnsvl.tourGuide.service.RewardsService;
import com.estbnsvl.tourGuide.service.TourGuideService;
import com.estbnsvl.tourGuide.model.user.User;

public class TestPerformance {

	@Before
	public void setup(){
		Locale.setDefault(Locale.UK);
	}

	@Test
	public void highVolumeTrackLocation() {
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(1000);

		GpsUtil gpsUtil = new GpsUtil();
		InternalUserApi internalUserApi = new InternalUserApi();
		internalUserApi.runInitializeInternalUsers();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);

		List<User> allUsers = tourGuideService.getAllUsers();

		Tracker tracker = new Tracker(tourGuideService);

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		tracker.trackAllUser(allUsers);

		stopWatch.stop();
		tracker.stopTracking();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() {
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100000);

		GpsUtil gpsUtil = new GpsUtil();
		InternalUserApi internalUserApi = new InternalUserApi();
		internalUserApi.runInitializeInternalUsers();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService,internalUserApi);

		Tracker tracker = new Tracker(tourGuideService);
		Reward reward = new Reward(rewardsService);

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		Attraction attraction = gpsUtil.getAttractions().get(0);
		List<User> allUsers = tourGuideService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

		reward.calculateRewardsAllUser(allUsers);

		stopWatch.stop();
		tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
}
