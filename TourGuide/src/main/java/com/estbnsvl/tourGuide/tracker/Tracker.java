package com.estbnsvl.tourGuide.tracker;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estbnsvl.tourGuide.service.TourGuideService;
import com.estbnsvl.tourGuide.model.user.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Tracker extends Thread {

	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private boolean stop = false;
	private final TourGuideService tourGuideService;

	public Tracker(TourGuideService tourGuideService){
		this.tourGuideService = tourGuideService;
	}

	public void runExecutorService(){
		executorService.submit(this);
	}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}

	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				log.debug("Tracker stopping");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();
			log.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			trackAllUser(users);
			stopWatch.stop();
			log.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				log.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void trackAllUser(List<User> allUser) {
		ExecutorService executorService = Executors.newFixedThreadPool(200);
		for (User user : allUser) {
			executorService.submit(new TrackUser(tourGuideService,user));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(25,TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			log.error("Tracking interrupted");
		}
	}

}