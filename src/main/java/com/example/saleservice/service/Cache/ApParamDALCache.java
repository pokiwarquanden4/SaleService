package com.example.saleservice.service.Cache;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApParamDALCache extends ApParamDAL{
    @Override
    public Object getApParam(Session session, String parName, String parType) {
        String key = "getApParam_" + parName + "_" + parType;
        Object cacheValue = CacheUtils.loadCache(key);
        if (cacheValue == null) {
            Object dbValue = super.getApParam(session, parName, parType);
            CacheUtils.setCache(key, dbValue);
            return dbValue;
        }
        return cacheValue;
    }
}
