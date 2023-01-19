package com.estbnsvl.tourGuide.service;

import com.estbnsvl.tourGuide.helper.InternalTestHelper;
import com.estbnsvl.tourGuide.model.user.User;
import com.estbnsvl.tourGuide.model.user.UserReward;
import com.estbnsvl.tourGuide.service.RewardsService;
import com.estbnsvl.tourGuide.service.TourGuideService;
import com.estbnsvl.tourGuide.tracker.Tracker;
import com.estbnsvl.tourGuide.userapi.InternalUserApi;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import rewardCentral.RewardCentral;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class RewardsServiceTest {
    //TODO Corriger les tests unitaires échouant par intermittence en raison d'une erreurConcurrentModificationException
//    @Autowired
//    private InternalUserApi internalUserApi;


    @Before
    public void setup(){
        Locale.setDefault(Locale.UK);
    }

    @Test
    public void userGetRewards() {
        InternalUserApi internalUserApi = new InternalUserApi();
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

        InternalTestHelper.setInternalUserNumber(10);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
        Tracker tracker = new Tracker(tourGuideService);


        User user = new User(UUID.randomUUID(), "jon", "000", "jon@com.estbnsvl.tourGuide.com");
        Attraction attraction = gpsUtil.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
        tracker.stopTracking();
        assertTrue(userRewards.size() == 1);
    }

    @Test
    public void isWithinAttractionProximity() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
        Attraction attraction = gpsUtil.getAttractions().get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    //	@Ignore // Needs fixed - can throw ConcurrentModificationException
//    @Test
//    public void nearAllAttractions() {
//
//        InternalUserApi internalUserApi = new InternalUserApi();
//        GpsUtil gpsUtil = new GpsUtil();
//        internalUserApi.runInitializeInternalUsers();
//
//
//        RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
//        rewardsService.setProximityBuffer(Integer.MAX_VALUE);
//
//        InternalTestHelper.setInternalUserNumber(1);
//        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
//        Tracker tracker = new Tracker(tourGuideService);
//
//
//        rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
//        List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
//        tracker.stopTracking();
//
//        assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
//    }

}
