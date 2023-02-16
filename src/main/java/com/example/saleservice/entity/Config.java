package com.example.saleservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "config")
public class Config {
    @Id
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;

    public Config(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
