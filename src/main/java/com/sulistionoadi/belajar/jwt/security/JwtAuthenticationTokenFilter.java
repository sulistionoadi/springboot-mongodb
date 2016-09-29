package com.sulistionoadi.belajar.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

public class JwtAuthenticationTokenFilter 
        extends UsernamePasswordAuthenticationFilter {
    
    private final Logger logger = 
            LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    public void doFilter(
            ServletRequest request, 
            ServletResponse response, 
            FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(this.tokenHeader);

        if(StringUtils.hasText(authToken) 
                && authToken.startsWith("Bearer ")) 
            authToken = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        logger.info("Username login is {}", username);

        String password = jwtTokenUtil.getPasswordFromToken(authToken);
        logger.info("Password login is {}", password);
        
        if (username != null 
                && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {
            
            logger.info("Security Context authentication is null");
            UserDetails userDetails = 
                    this.userDetailsService.loadUserByUsername(username);
            
            logger.info("Validate Password from Token [{} vs {}]", userDetails.getPassword(), password);
            
            if (jwtTokenUtil.validateToken(authToken, userDetails) && 
                    userDetails.getPassword().equals(password)) {
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(httpRequest));
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}