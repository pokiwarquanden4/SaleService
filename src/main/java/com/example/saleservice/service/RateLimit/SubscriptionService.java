package com.example.saleservice.service.RateLimit;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubscriptionService {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    // Khởi tạo bucket4j
    public Bucket resolveBucket(String keys) {
        return buckets.computeIfAbsent(keys, this::newBucket);
    }
    //giới hạn lượng gửi request khi truyền vào key gói giới hạn
    private Bucket newBucket(String key) {
        SubscriptionPlan pricingPlan = resolveSubscriptionPlanBylimit(key);
        int requestTime = pricingPlan.getRequestLimit();
        Bandwidth limit = Bandwidth.classic(requestTime, Refill.intervally(requestTime, Duration.ofSeconds(60)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
    //Lấy ra gói phân cấp dựa vào key gói giới hạn
    private SubscriptionPlan resolveSubscriptionPlanBylimit(String subscriptionKey) {
        if (subscriptionKey.equals("basic")) {
            return SubscriptionPlan.SUBSCRIPTION_PROFESSIONAL;
        } else if (subscriptionKey.equals("pro")) {
            return SubscriptionPlan.SUBSCRIPTION_BASIC;
        }

        return SubscriptionPlan.SUBSCRIPTION_FREE;
    }
}
