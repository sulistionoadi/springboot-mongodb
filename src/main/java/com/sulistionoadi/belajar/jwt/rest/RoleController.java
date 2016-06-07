/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.rest;

import com.sulistionoadi.belajar.jwt.dao.RoleDao;
import com.sulistionoadi.belajar.jwt.domain.Role;
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
@RequestMapping("/api/role")
public class RoleController {
    
    private final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
    
    @Autowired
    private RoleDao roleDao;
    
    @RequestMapping(method = RequestMethod.GET)
    public List<Role> getAllRole(){
        LOGGER.info("--=== {} ===--", "Find All Role");
        return roleDao.findAll();
    }
    
    @RequestMapping(value="/{key}/{value}", method = RequestMethod.GET)
    public Role findByUsername(@PathVariable String key, @PathVariable String value){
        if(key.equalsIgnoreCase("id")){
            LOGGER.info("--=== {} [{}]===--", "Find Role byId", value);
            return roleDao.findOne(value);
        } else if(key.equalsIgnoreCase("name")){
            LOGGER.info("--=== {} [{}]===--", "Find Role byName", value);
            return roleDao.findByName(value);
        } else {
            LOGGER.error("--=== {} ===--", "Invalid Search Key Parameter. allowed key is [id, name]");
            return null;
        }
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public void save(@RequestBody Role role){
        LOGGER.info("--=== {} [{}]===--", "Save Role", role.toString());
        roleDao.save(role);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable String id, @RequestBody Role role){
        Role r = roleDao.findOne(id);
        if(r!=null){
            role.setId(r.getId());
            
            LOGGER.info("--=== {} [{}]===--", "Update Role", role.toString());
            roleDao.save(role);
        }
        
        LOGGER.info("--=== {} [{}]===--", "Role not found ! byId ", id);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id){
        Role r = roleDao.findOne(id);
        if(r!=null){
            
            LOGGER.info("--=== {} [{}]===--", "Delete Role", id);
            roleDao.delete(r);
        }
        
        LOGGER.info("--=== {} [{}]===--", "For Delete, Role not found ! byId ", id);
    }

}