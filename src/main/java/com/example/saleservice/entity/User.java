package com.example.saleservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;
    @Column(name = "userName")
    private String userName;
    @Column(name = "passWord")
    private String passWord;
    private String keylimit;
    @Column(name = "role")
    private String role;

    public void getKeylimit(String keylimit) {
        this.keylimit = keylimit;
    }
}
