package com.example.saleservice.repository;

import com.example.saleservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {

    @Query(nativeQuery = true, value = "select keylimit from users where id = :id")
    String findKeylimitByUserId(@Param("id")Long id);

    Optional<User> findByUserName(String username);

}
