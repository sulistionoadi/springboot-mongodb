/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.rest;

import com.sulistionoadi.belajar.jwt.dao.UserDao;
import com.sulistionoadi.belajar.jwt.domain.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adi
 */

@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserDao userDao;
    
    @RequestMapping("/user")
    public List<User> getAllUser(){
        return userDao.findAll();
    }
    
    @RequestMapping("/user/{username}")
    public User getAllUser(){
        return userDao.findAll();
    }
    
}
