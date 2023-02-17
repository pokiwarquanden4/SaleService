package com.example.demobucket4j.interceptor;


import com.example.saleservice.entity.User;
import com.example.saleservice.service.AuthService.AuthService;
import com.example.saleservice.service.RateLimit.SubscriptionService;
import com.google.gson.Gson;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    AuthService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String json = readJsonFromRequest(request);

        User user = new Gson().fromJson(json, User.class);
        Long userId = user.getID();

        String keylimit = userService.findKeylimitByUserId(userId);
        if (keylimit == null || keylimit.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Request");
            return false;
        }

        if (!isRequestAllowed(keylimit, response)) {
            return false;
        }

        return true;
    }

    private String readJsonFromRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private boolean isRequestAllowed(String keylimit, HttpServletResponse response) throws IOException {
        Bucket tokenBucket = subscriptionService.resolveBucket(keylimit);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("Retry After Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "You have exhausted your API Request Quota");
            return false;
        }
    }

}
