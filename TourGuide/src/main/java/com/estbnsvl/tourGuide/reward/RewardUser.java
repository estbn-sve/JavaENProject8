package com.estbnsvl.tourGuide.reward;

import com.estbnsvl.tourGuide.model.user.User;
import com.estbnsvl.tourGuide.service.RewardsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RewardUser extends Thread {

    private RewardsService rewardsService;
    private User user;

    public RewardUser(RewardsService rewardsService, User user){
        this.rewardsService = rewardsService;
        this.user = user;
    }

    @Override
    public void run(){
        log.info("Reward started for the user : {}",user.getUserName());
        rewardsService.calculateRewards(user);
        log.info("Reward end for the user : {}",user.getUserName());
    }
}
