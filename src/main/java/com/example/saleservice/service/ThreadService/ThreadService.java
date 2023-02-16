package com.example.saleservice.service.ThreadService;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
@Getter
@Setter
public class ThreadService extends TimerTask{
    private final Long expired = 60000l;
    ConcurrentHashMap<String, Map<String, Timestamp>> configCache;
    ArrayList<String> removeList = new ArrayList<>();
    RemoveListCallback removeListCallback;
    public void setRemoveList(ArrayList<String> removeList) {
        this.removeList = removeList;
        if(removeListCallback != null){
            removeListCallback.onRemoveListChanged(removeList);
        }
    }
    public void run() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ArrayList<String> newRemoveList = new ArrayList<>();
        for (Map.Entry<String, Map<String, Timestamp>> entry : configCache.entrySet()) {
            String key = entry.getKey();
            Map<String, Timestamp> innerMap = entry.getValue();
            for (Map.Entry<String, Timestamp> innerEntry : innerMap.entrySet()) {
                Timestamp innerValue = innerEntry.getValue();
                if(checkDiffrent(timestamp, innerValue)){
                    newRemoveList.add(key);
                }
            }
        }
        setRemoveList(newRemoveList);
    }

    public boolean checkDiffrent(Timestamp timestamp, Timestamp now){
        Long diffrent = ((timestamp.getTime() - now.getTime()));
        if(diffrent > expired){
            return true;
        }else {
            return false;
        }
    }

    public ThreadService(ConcurrentHashMap configCache) {
        this.configCache = configCache;
    }

    public interface RemoveListCallback {
        void onRemoveListChanged(ArrayList<String> removeList);
    }
}


