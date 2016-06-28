/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.jwt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adi
 */
public class CustomRequestHandler extends HttpServletRequestWrapper {

    private final Logger log = LoggerFactory.getLogger(CustomRequestHandler.class);

    public CustomRequestHandler(HttpServletRequest request) throws IOException {
        super(request);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
    
    public byte[] getRequestBody() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String body = IOUtils.toString(super.getInputStream(), "UTF-8");
        Object originalPayload = parseToken(body);
        
        String sOrigPayload = mapper.writeValueAsString(originalPayload);
        log.info("ORIGINAL PAYLOAD FROM TOKEN [{}]", sOrigPayload);
        
        return sOrigPayload.getBytes("UTF-8");
    }
    
    public Object parseToken(String token) {
        log.info("TOKEN TO PARSE [{}]", token);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Claims body = Jwts.parser()
                    .setSigningKey("bismillah".getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();

            log.info("JSON PAYLOAD [{}]", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));
            
            return body;
        } catch (JwtException | ClassCastException | JsonProcessingException | UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getRequestBody());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener rl) {
                throw new RuntimeException("Not implemented");
            }
        };
        return servletInputStream;
    }

}
