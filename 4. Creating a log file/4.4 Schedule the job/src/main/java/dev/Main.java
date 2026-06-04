package dev;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Create a scheduler with one thread
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Wrap your main logic in a Runnable
        Runnable task = () -> {
            try {
                log.info("Running job at {}", LocalDateTime.now());
                runJob();
            } catch (Exception e) {
                log.error("Error occurred while executing scheduled job", e);
            }
            log.debug("Job finished (success or error) at {}", LocalDateTime.now());
        };

        // Schedule it: initial delay 0, repeat every 24 hours
        scheduler.scheduleAtFixedRate(task, 0, 24, TimeUnit.HOURS);
    }

    private static void runJob() {
        System.out.println("Executing main job logic...");
        // Your Mailgun logic goes here,
        // e.g. fetch Mailgun data, write to file, etc.
    }


}