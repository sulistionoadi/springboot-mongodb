/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.handler;

import com.sulistionoadi.belajar.jwt.security.JwtTokenUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author adi
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response, Authentication a)
            throws IOException, ServletException {
        handle(request, response, a);
        clearAuthenticationAttributes(request);
    }

    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        try {
            String username = authentication.getName();
            logger.debug("Handler Login Success for username [{}]",
                    username);

            final UserDetails userDetails
                    = userDetailsService.loadUserByUsername(username);
            final String token = jwtTokenUtil
                    .generateToken(userDetails);

            response.setHeader(tokenHeader, token);
            response.setHeader(HttpHeaders.LOCATION,
                    request.getServletContext()
                    .getContextPath() + "/#/");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            HttpSession session = request.getSession();
            session.setAttribute(
                    WebAttributes.AUTHENTICATION_EXCEPTION,
                    ex.getMessage());

            try (PrintWriter writer = response.getWriter()) {
                writer.write("{\"code\":\"" + response.getStatus()
                        + "\", \"status\":\"ERR\", "
                        + "\"message\":\"" + ex.getMessage() + "\"}");
                writer.flush();
                writer.close();
            }
        }
    }

    protected void clearAuthenticationAttributes(
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(
                WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
