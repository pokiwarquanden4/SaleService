package com.example.saleservice.repository;

import com.example.saleservice.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigCacheRepo extends JpaRepository<Config, String> {
    @Query(value = "SELECT * FROM config WHERE `key` = :valueFromDatabase ", nativeQuery = true)
    Config findConfig(String valueFromDatabase);
}