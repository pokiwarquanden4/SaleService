package com.example.saleservice.service.RateLimit;

import com.example.saleservice.service.RateLimit.RateLimit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RateLimitService {
    private ArrayList<RateLimit> rateLimits = new ArrayList<>();

    public boolean startRateLimit(String URL,int limitRequest, int limitSecond){
        if(!checkDuplicate(URL)){
            createRateLimit(URL, limitRequest, limitSecond);
        }
        return consume(URL);
    }

    public void createRateLimit(String URL, int limitRequest, int limitSecond){
        RateLimit rateLimit = new RateLimit(URL, limitRequest, limitSecond);
        rateLimits.add(rateLimit);
    }

    public boolean checkDuplicate(String URL){
        for (RateLimit rateLimit: rateLimits){
            if (rateLimit.getURL().equalsIgnoreCase(URL)){
                return true;
            }
        }
        return false;
    }

    public boolean consume(String URL){
        for (RateLimit rateLimit: rateLimits){
            if (rateLimit.getURL().equalsIgnoreCase(URL)){
                return rateLimit.consume();
            }
        }
        return false;
    }
}
