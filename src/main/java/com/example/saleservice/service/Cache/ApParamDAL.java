package com.example.saleservice.service.Cache;

import com.example.saleservice.repository.ConfigCacheRepo;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

public class ApParamDAL {
    @Autowired
    ConfigCacheRepo configCacheRepo;
    public Object getApParam(Session session, String parName, String parType){
        String key = "getApParam_" + parName + "_" + parType;
        return configCacheRepo.findConfig(key);
    }
}
