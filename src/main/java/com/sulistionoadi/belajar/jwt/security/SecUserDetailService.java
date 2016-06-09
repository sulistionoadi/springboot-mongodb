/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.security;

import com.sulistionoadi.belajar.jwt.dao.UserDao;
import com.sulistionoadi.belajar.jwt.domain.Permission;
import com.sulistionoadi.belajar.jwt.domain.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *
 * @author adi
 */

@Component
public class SecUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        } else{
            UserDetails details = new SecManUserDetails(user.getId(), user.getUsername(), user.getPassword(), 
                    mapToGrantedAuthorities(new ArrayList<>(user.getRole().getPermissions())), true);
            return details;
        }
    }
    
    private List<GrantedAuthority> mapToGrantedAuthorities(List<Permission> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Permission p : permissions){
            authorities.add(new SimpleGrantedAuthority(p.getValue()));
        }
        return authorities;
    }
    
}
