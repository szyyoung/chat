package com.example.restful.dao;

import com.example.restful.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends CrudRepository<User, Long> {

    Optional<User> findByAccount(String account);


    Optional<User> findByAccountAndPassword(String account,String password);
}
