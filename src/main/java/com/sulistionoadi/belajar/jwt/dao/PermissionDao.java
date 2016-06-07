/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.dao;

import com.sulistionoadi.belajar.jwt.domain.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author adi
 */
public interface PermissionDao extends MongoRepository<Permission, String> {
    
    public Permission findByValue(String value); 
    
}