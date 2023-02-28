package com.example.saleservice.service.Cache;

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
public class CacheUtils {
    @Autowired
    private static ConfigCacheRepo configCacheRepo;
    private static ConcurrentHashMap<String, Map<Object, Timestamp>> configCache = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Map<Object, Timestamp>> getConfigCache() {
        return configCache;
    }

    public static Object loadCache(String key){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(configCache.containsKey(key)){
            Map<Object, Timestamp> innerMap= configCache.get(key);
            for (Map.Entry<Object, Timestamp> innerEntry : innerMap.entrySet()) {
                Object value = innerEntry.getKey();
                Timestamp innerTimestamp = innerEntry.getValue();
                if(timestamp.getTime() - innerTimestamp.getTime() > 90000){
                    configCache.remove(key);
                    return null;
                }else {
                    return value;
                }
            }
        }
        return null;
    }

    public static boolean setCache(String key, Object value){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (value != null && key != null){
            Map<Object, Timestamp> map = new HashMap<>();
            map.put(value, timestamp);
            configCache.put(key, map);
            return true;
        }else {
            return false;
        }
    }



    public void removeCache(ArrayList<String> removeList){
        for (String removeKey: removeList){
            configCache.remove(removeKey);
        }
    }

//    @Scheduled(fixedDelay = 60000L)
//    void handleExpired(){
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        ArrayList<String> newRemoveList = new ArrayList<>();
//        for (Map.Entry<String, Map<String, Timestamp>> entry : configCache.entrySet()) {
//            String key = entry.getKey();
//            Map<String, Timestamp> innerMap = entry.getValue();
//            for (Map.Entry<String, Timestamp> innerEntry : innerMap.entrySet()) {
//                Timestamp innerValue = innerEntry.getValue();
//                if(checkDifferent(timestamp, innerValue)){
//                    newRemoveList.add(key);
//                }
//            }
//        }
//        removeCache(newRemoveList);
//    }
//
//    public boolean checkDifferent(Timestamp timestamp, Timestamp now){
//        Long different = ((timestamp.getTime() - now.getTime()));
//        if(different > 30000l){
//            return true;
//        }else {
//            return false;
//        }
//    }
}

