/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.rest;

import com.sulistionoadi.belajar.jwt.dao.UserDao;
import com.sulistionoadi.belajar.jwt.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adi
 */

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserDao userDao;
    
    @RequestMapping(method = RequestMethod.GET)
    public Page<User> getAllUser(Pageable pageable){
        LOGGER.info("--=== {} ===--", "Find All User");
        return userDao.findAll(pageable);
    }
    
    @RequestMapping(value="/{key}/{value}", method = RequestMethod.GET)
    public User findByUsername(@PathVariable String key, @PathVariable String value){
        if(key.equalsIgnoreCase("id")){
            LOGGER.info("--=== {} [{}]===--", "Find User byId", value);
            return userDao.findOne(value);
        } else if(key.equalsIgnoreCase("username")){
            LOGGER.info("--=== {} [{}]===--", "Find User byUsername", value);
            return userDao.findByUsername(value);
        } else {
            LOGGER.error("--=== {} ===--", "Invalid Search Key Parameter. allowed key is [id, username]");
            return null;
        }
    }
    
    
    @RequestMapping(method = RequestMethod.POST)
    public void save(@RequestBody User user){
        LOGGER.info("--=== {} [{}]===--", "Save User", user.toString());
        userDao.save(user);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable String id, @RequestBody User user){
        User u = userDao.findOne(id);
        if(u!=null){
            user.setId(u.getId());
            
            LOGGER.info("--=== {} [{}]===--", "Update User", user.toString());
            userDao.save(user);
        }
        
        LOGGER.info("--=== {} [{}]===--", "User not found ! byId ", id);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id){
        User u = userDao.findOne(id);
        if(u!=null){
            
            LOGGER.info("--=== {} [{}]===--", "Delete User", id);
            userDao.delete(u);
        }
        
        LOGGER.info("--=== {} [{}]===--", "For Delete, User not found ! byId ", id);
    }

}
