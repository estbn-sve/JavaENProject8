package com.estbnsvl.tourGuide.reward;

import com.estbnsvl.tourGuide.model.user.User;
import com.estbnsvl.tourGuide.service.RewardsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Reward extends Thread {

    private final RewardsService rewardsService;

    public Reward(RewardsService rewardsService){
        this.rewardsService = rewardsService ;
    }

    public void calculateRewardsAllUser (List<User> allUser){
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (User user : allUser){
            executorService.submit(new RewardUser(rewardsService,user));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(25, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("Reward interrupted");
        }
    }
}
