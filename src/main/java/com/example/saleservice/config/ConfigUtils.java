package com.example.saleservice.config;

import com.example.saleservice.entity.Config;
import com.example.saleservice.repository.ConfigCacheRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Getter
@EnableScheduling
public class ConfigUtils{
    @Autowired
    ConfigCacheRepo configCacheRepo;
    ConcurrentHashMap<String, Map<String, Timestamp>> configCache = new ConcurrentHashMap<>();

    public String getConfig(String inputKey){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       if(configCache.containsKey(inputKey)){
           Map<String, Timestamp> innerMap= configCache.get(inputKey);
           for (Map.Entry<String, Timestamp> innerEntry : innerMap.entrySet()) {
               String value = innerEntry.getKey();
               Timestamp innerValue = innerEntry.getValue();
               if(timestamp.getTime() > innerValue.getTime()){
                   return value;
               }else {
                   configCache.remove(inputKey);
                   return addItems(timestamp, inputKey);
               }
           }
       }else {
           return addItems(timestamp, inputKey);
       }
        return "Query Failed";
    }

    public String addItems(Timestamp timestamp, String inputKey){
        System.out.println("Query To DB");
        Config config = configCacheRepo.findConfig(inputKey);
        if (config != null) {
            Map<String, Timestamp> map = new HashMap<>();
            map.put(config.getValue(), timestamp);
            configCache.put(inputKey, map);
        }
        return config.getValue();
    }

    public void removeCache(ArrayList<String> removeList){
        System.out.println("Clear: " + removeList);
        for (String removeKey: removeList){
            configCache.remove(removeKey);
        }
    }

    @Scheduled(fixedDelay = 60000L)
    void handleExpired(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ArrayList<String> newRemoveList = new ArrayList<>();
        for (Map.Entry<String, Map<String, Timestamp>> entry : configCache.entrySet()) {
            String key = entry.getKey();
            Map<String, Timestamp> innerMap = entry.getValue();
            for (Map.Entry<String, Timestamp> innerEntry : innerMap.entrySet()) {
                Timestamp innerValue = innerEntry.getValue();
                if(checkDifferent(timestamp, innerValue)){
                    newRemoveList.add(key);
                }
            }
        }
        removeCache(newRemoveList);
    }

    public boolean checkDifferent(Timestamp timestamp, Timestamp now){
        Long different = ((timestamp.getTime() - now.getTime()));
        if(different > 30000l){
            return true;
        }else {
            return false;
        }
    }
}

