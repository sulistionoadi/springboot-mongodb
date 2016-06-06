/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.dao;

import com.sulistionoadi.belajar.jwt.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author adi
 */
public interface UserDao extends MongoRepository<User, String>{
    public User findByUsername(String username);
}
