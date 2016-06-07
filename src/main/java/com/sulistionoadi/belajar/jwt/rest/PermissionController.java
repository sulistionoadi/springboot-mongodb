/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.rest;

import com.sulistionoadi.belajar.jwt.dao.PermissionDao;
import com.sulistionoadi.belajar.jwt.domain.Permission;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/permission")
public class PermissionController {
    
    private final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);
    
    @Autowired
    private PermissionDao permissionDao;
    
    @RequestMapping(method = RequestMethod.GET)
    public List<Permission> getAllPermission(){
        LOGGER.info("--=== {} ===--", "Find All Permission");
        return permissionDao.findAll();
    }
    
    @RequestMapping(value="/{key}/{value}", method = RequestMethod.GET)
    public Permission findPermission(@PathVariable String key, @PathVariable String value){
        if(key.equalsIgnoreCase("id")){
            LOGGER.info("--=== {} [{}]===--", "Find Permission byId", value);
            return permissionDao.findOne(value);
        } else if(key.equalsIgnoreCase("value")){
            LOGGER.info("--=== {} [{}]===--", "Find Permission byValue", value);
            return permissionDao.findByValue(value);
        } else {
            LOGGER.error("--=== {} ===--", "Invalid Search Key Parameter. allowed key is [id, value]");
            return null;
        }
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public void save(@RequestBody Permission permission){
        LOGGER.info("--=== {} [{}]===--", "Save Permission", permission.toString());
        permissionDao.save(permission);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable String id, @RequestBody Permission permission){
        Permission p = permissionDao.findOne(id);
        if(p!=null){
            permission.setId(p.getId());
            
            LOGGER.info("--=== {} [{}]===--", "Update Permission", permission.toString());
            permissionDao.save(permission);
        }
        
        LOGGER.info("--=== {} [{}]===--", "Permission not found ! byId ", id);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id){
        Permission p = permissionDao.findOne(id);
        if(p!=null){
            LOGGER.info("--=== {} [{}]===--", "Delete Permission", id);
            permissionDao.delete(p);
        }
        
        LOGGER.info("--=== {} [{}]===--", "For Delete, Permission not found ! byId ", id);
    }

}