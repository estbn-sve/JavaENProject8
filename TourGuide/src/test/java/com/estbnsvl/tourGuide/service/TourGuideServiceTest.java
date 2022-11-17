package com.estbnsvl.tourGuide.service;

import com.estbnsvl.tourGuide.helper.InternalTestHelper;
import com.estbnsvl.tourGuide.model.user.User;
import com.estbnsvl.tourGuide.tracker.Tracker;
import com.estbnsvl.tourGuide.userapi.InternalUserApi;
import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import rewardCentral.RewardCentral;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TourGuideServiceTest extends TestCase {

    @Before
    public void setup(){
        Locale.setDefault(Locale.UK);
    }

    @InjectMocks
    private TourGuideService tourGuideService;

    @Mock
    private GpsUtil gpsUtil;

    @Mock
    private RewardsService rewardsService;

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

    @Test
    public void GetUserLocation_Should_Pass_When_User_Have_VisitedLocation() {
        //Given
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),new Location(3,3),new Date());
        User user = new User(new UUID(1,1),"internalUserTest1","","");
        user.addToVisitedLocations(visitedLocation);
        //When
        VisitedLocation result = tourGuideService.getUserLocation(user);
        //Then
        assertThat(visitedLocation).isEqualTo(result);
    }

    @Test
    public void GetUserLocation_Should_Pass_When_User_Have_No_VisitedLocation() {
        //Given
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),new Location(3,3),new Date());
        User user = new User(new UUID(1,1),"internalUserTest1","","");
        when(gpsUtil.getUserLocation(any())).thenReturn(visitedLocation);
        doNothing().when(rewardsService).calculateRewards(user);
        //When
        VisitedLocation result = tourGuideService.getUserLocation(user);
        //Then
        assertThat(visitedLocation).isEqualTo(result);
    }

}
