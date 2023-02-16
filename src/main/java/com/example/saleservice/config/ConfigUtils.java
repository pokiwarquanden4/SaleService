package com.example.saleservice.config;

import com.example.saleservice.entity.Config;
import com.example.saleservice.repository.ConfigCacheRepo;
import com.example.saleservice.service.ThreadService.ThreadService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Getter
public class ConfigUtils implements ThreadService.RemoveListCallback{
    @Autowired
    ConfigCacheRepo configCacheRepo;
    ConcurrentHashMap<String, Map<String, Timestamp>> configCache = new ConcurrentHashMap<>();
    Timer timer = new Timer();
    int delay = 0;
    int period = 1 * 30 * 1000;

    ThreadService threadService;

    public String getConfig(String inputKey){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       if(configCache.containsKey(inputKey)){
           Map<String, Timestamp> innerMap= configCache.get(inputKey);
           for (Map.Entry<String, Timestamp> innerEntry : innerMap.entrySet()) {
               String key = innerEntry.getKey();
               Timestamp innerValue = innerEntry.getValue();
               if(timestamp.getTime() > innerValue.getTime()){
                   return key;
               }
           }
       }else {
           Config config = configCacheRepo.findConfig(inputKey);
           if (config != null) {
               Map<String, Timestamp> map = new HashMap<>();
               map.put(config.getValue(), timestamp);
               configCache.put(inputKey, map);
               threadService.setConfigCache(configCache);

               //recursion
               return getConfig(inputKey);
           }
       }
        return "Query Failed";
    }
    @Bean
    public void checkExpired(){
        threadService = new ThreadService(configCache);
        threadService.setRemoveListCallback(this);
        timer.scheduleAtFixedRate(threadService,delay, period);
    }

    public void removeCache(ArrayList<String> removeList){
        for (String removeKey: removeList){
            configCache.remove(removeKey);
        }
        threadService.setConfigCache(configCache);
    }

    @Override
    public void onRemoveListChanged(ArrayList<String> removeList) {
        removeCache(removeList);
    }
}

