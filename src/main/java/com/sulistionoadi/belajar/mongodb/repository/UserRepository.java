/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.mongodb.repository;

import com.sulistionoadi.belajar.mongodb.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author adi
 */
public interface UserRepository extends MongoRepository<User, String>{

}
