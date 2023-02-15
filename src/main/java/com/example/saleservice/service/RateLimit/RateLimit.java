package com.example.saleservice.service.RateLimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@NoArgsConstructor
public class RateLimit {
    private Bucket bucket;
    private String URL;
    private int limitRequest;
    private int limitSecond;

    public RateLimit(String URL, int limitRequest, int limitSecond) {
        this.URL = URL;
        this.limitRequest = limitRequest;
        this.limitSecond = limitSecond;

        generateToken(limitRequest, limitSecond);
    }

    public void generateToken(int limitRequest, int limitSecond){
        Refill refill = Refill.of(limitRequest, Duration.ofSeconds(limitSecond));
        bucket = Bucket4j.builder().addLimit(Bandwidth.classic(limitRequest, refill)).build();
    }

    public boolean consume(){
        return bucket.tryConsume(1);
    }
}
