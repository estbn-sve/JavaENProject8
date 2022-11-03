package com.estbnsvl.tourGuide.controller;

import com.estbnsvl.tourGuide.model.user.User;
import com.estbnsvl.tourGuide.service.TourGuideService;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TourGuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourGuideService tourGuideService;

    @Test
    public void getIndex_Should_Return_Ok() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void getLocation_Should_Return_Ok() throws Exception {
        User user = new User(new UUID(1,1),"","","");
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),new Location(3,3),new Date());
        when(tourGuideService.getUser("internalUserTest1")).thenReturn(user);
        when(tourGuideService.getUserLocation(user)).thenReturn(visitedLocation);
        mockMvc.perform(get("/getLocation/internalUserTest1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getLocation_Should_Return_NotFound() throws Exception {
        User user = new User(new UUID(1,1),"","","");
        when(tourGuideService.getUser("internalUserTest1")).thenReturn(user);
        when(tourGuideService.getUserLocation(user)).thenThrow(new NullPointerException());
        mockMvc.perform(get("/getLocation/internalUserTest1"))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void getClosestAttractions_Should_Return_Ok() throws Exception {
//        User user = new User(new UUID(1,1),"","","");
//        when(tourGuideService.getUser("internalUserTest1")).thenReturn(user);
//        when(tourGuideService.getClosestAttractions(user)).thenReturn(new ArrayList<>());
//        mockMvc.perform(get("/getClosestAttractions/internalUserTest1"))
//                .andExpect(status().isOk());
//    }

//    @Test
//    public void getClosestAttractions_Should_Return_NotFound() throws Exception {
//        User user = new User(new UUID(1,1),"","","");
//        when(tourGuideService.getUser("internalUserTest1")).thenReturn(user);
//        when(tourGuideService.getClosestAttractions(user)).thenThrow(new NullPointerException());
//        mockMvc.perform(get("/getClosestAttractions/internalUserTest1"))
//                .andExpect(status().isNotFound());
//    }

    @Test
    public void getRewards_Should_Return_Ok() throws Exception {
        User user = new User(new UUID(1,1),"","","");
        when(tourGuideService.getUser("internalUserTest1")).thenReturn(user);
        when(tourGuideService.getUserRewards(user)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/getRewards/internalUserTest1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getRewards_Should_Return_NotFound() throws Exception {
        User user = new User(new UUID(1,1),"","","");
        when(tourGuideService.getUser("internalUserTest1")).thenReturn(user);
        when(tourGuideService.getUserRewards(user)).thenThrow(new NullPointerException());
        mockMvc.perform(get("/getRewards/internalUserTest1"))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void getAllCurrentLocations_Should_Return_Ok() throws Exception {
//        when(tourGuideService.getAllCurrentLocations()).thenReturn(new ArrayList<>());
//        mockMvc.perform(get("/getAllCurrentLocations"))
//                .andExpect(status().isOk());
//    }

    @Test
    public void getTripDeals_Should_Return_Ok() throws Exception {
        User user = new User(new UUID(1,1),"","","");
        when(tourGuideService.getUser("internalUserTest1")).thenReturn(user);
        when(tourGuideService.getTripDeals(user)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/getTripDeals/internalUserTest1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTripDeals_Should_Return_NotFound() throws Exception {
        User user = new User(new UUID(1,1),"","","");
        when(tourGuideService.getUser("internalUserTest1")).thenReturn(user);
        when(tourGuideService.getTripDeals(user)).thenThrow(new NullPointerException());
        mockMvc.perform(get("/getTripDeals/internalUserTest1"))
                .andExpect(status().isNotFound());
    }

}
