/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * @author adi
 */

@SpringBootApplication
@EnableMongoRepositories
public class BelajarApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BelajarApplication.class, args);
    }
    
}
