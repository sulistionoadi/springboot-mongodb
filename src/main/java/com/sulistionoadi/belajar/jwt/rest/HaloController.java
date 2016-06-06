/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adi
 */

@RestController
@RequestMapping("/api")
public class HaloController {
    
    @RequestMapping("/halo")
    public String getAllUser(){
        return "Welcome Dude...";
    }
    
}
