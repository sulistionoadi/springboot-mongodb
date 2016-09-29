package com.sulistionoadi.belajar.jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_AUDIENCE = "audience";
    private static final String CLAIM_KEY_CREATED = "created";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;
    
    @Autowired
    private EncryptionUtil encryptionUtil;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims==null) return null;
            username = claims.getSubject();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            username = null;
        }
        return username;
    }
    
    public String getPasswordFromToken(String token) {
        String password;
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims==null) return null;
            String encryptedSecret = claims.get("secret").toString();
            password = encryptionUtil.decrypt(encryptedSecret);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            password = null;
        }
        return password;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

////    public String getAudienceFromToken(String token) {
////        String audience;
////        try {
////            final Claims claims = getClaimsFromToken(token);
////            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
////        } catch (Exception e) {
////            audience = null;
////        }
////        return audience;
////    }
//
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(UserDetails userDetails) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_AUDIENCE, "web");
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put("secret", encryptionUtil.encrypt(userDetails.getPassword()));
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, new Date())
                && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        SecManUserDetails user = (SecManUserDetails) userDetails;
        final String username = getUsernameFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token));
    }
}