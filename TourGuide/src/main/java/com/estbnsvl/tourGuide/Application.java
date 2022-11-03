package com.estbnsvl.tourGuide;

import com.estbnsvl.tourGuide.tracker.Tracker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new Thread( ()->{
            for (int i = 0; i<5; i++){
                System.out.println("coucou");
            }
        }

        ).run();
        System.out.println("Fin");
        Runnable run = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i<5; i++){
                    System.out.println("deuxieme runnable");
                }
            }
        };
        run.run();
        System.out.println("fin runnable");
    }
}
