package com.dio.dio_spring_security_jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dio.dio_spring_security_jwt.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT e FROM User e JOIN FETCH e.roles WHERE e.username= (:username)")
    public User findByUsername(@Param("username") String username);
}