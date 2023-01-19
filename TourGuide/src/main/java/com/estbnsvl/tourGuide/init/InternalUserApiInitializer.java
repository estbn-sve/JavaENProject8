package com.estbnsvl.tourGuide.init;

import com.estbnsvl.tourGuide.userapi.InternalUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InternalUserApiInitializer implements CommandLineRunner {

    @Autowired
    private InternalUserApi internalUserApi;

    @Override
    public void run(String... args) throws Exception {
        internalUserApi.runInitializeInternalUsers();
    }
}
