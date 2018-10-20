package com.abhishekbhalla.abweb.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DynoRefreshService implements Runnable {
    private ScheduledExecutorService executor;
    private AtomicLong aLong = new AtomicLong();
    private HttpGet httpGet;

    private long lastRequestTime;

    private static final String URL_TO_CALL = "www.abhishekbhalla.com/ping";
    private static final long MAX_WAIT = 1200000;

    private static final Logger LOGGER = LoggerFactory.getLogger(DynoRefreshService.class);

    @PostConstruct
    private void init() {
        setLastRequestTime();
        httpGet = new HttpGet(URL_TO_CALL);
        httpGet.addHeader("origin", "http://www.abhishekbhalla.com/ping" );
        httpGet.addHeader("referer", "self");
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this, 27, 9, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        long timeElapsed = System.currentTimeMillis()-getLastRequestTime();
        if (timeElapsed >= MAX_WAIT) {
            LOGGER.info("Time Elapsed: " + timeElapsed/1000 + ", Self executed calls: " + aLong.incrementAndGet() + ", Status Code: " + executeCall());
            setLastRequestTime();
        }
    }

    private int executeCall() {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(httpGet);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
//            LOGGER.info("get exception");
            e.printStackTrace();
        }
        return 0;
    }

    public long getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime() {
        this.lastRequestTime = System.currentTimeMillis();
    }

    @PreDestroy
    private void destroy() {
        executor.shutdownNow();
    }
}
