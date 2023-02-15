package com.example.saleservice.config;

import com.example.saleservice.config.MySQL.MySqlConfiguration;
import org.springframework.context.annotation.Import;

@org.springframework.context.annotation.Configuration
@Import({MySqlConfiguration.class})
public class Configuration {
}
