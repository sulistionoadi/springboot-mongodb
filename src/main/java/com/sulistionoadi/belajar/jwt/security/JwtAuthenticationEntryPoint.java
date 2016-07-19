/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Component;

/**
 *
 * @author adi
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable{
    
    private static final long serialVersionUID = -8970718410437077606L;
    
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ae) throws IOException, ServletException {
        res.setStatus(HttpStatus.UNAUTHORIZED.value());

        HttpSession session = req.getSession();
        session.setAttribute(
                WebAttributes.AUTHENTICATION_EXCEPTION, 
                ae.getMessage());
        
        try (PrintWriter writer = res.getWriter()) {
            writer.write("{\"code\":\""+res.getStatus()
                    + "\", \"status\":\"ERR\", "
                    + "\"message\":\""+ae.getMessage()+"\"}");
            writer.flush();
            writer.close();
        }
        
        res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        res.setHeader(HttpHeaders.LOCATION, 
                req.getServletContext()
                        .getContextPath() + "/login");
    }
    
}
