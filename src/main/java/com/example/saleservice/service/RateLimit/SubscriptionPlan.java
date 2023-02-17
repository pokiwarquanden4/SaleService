package com.example.saleservice.service.RateLimit;

public enum SubscriptionPlan {
    //Phân chia cấp độ gửi request
    SUBSCRIPTION_FREE(2),
    SUBSCRIPTION_BASIC(10),
    SUBSCRIPTION_PROFESSIONAL(20);

    private final int requestLimit;

    SubscriptionPlan(int requestLimit) {
        this.requestLimit = requestLimit;

    }
    public int getRequestLimit() {
        return this.requestLimit;
    }


}
