package com.JWTAuth.kom.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	private static final long EXPIRE_DURATION = 24*60*60*1000; //24h
	
	@Value("${app.jwt.secret}")
	private String secretKey;
	
	
	public String generateAccessToken(User user) {
		
		return Jwts.builder()
				.setSubject(user.getId()+ ","+ user.getEmail())
				.claim("roles", user.getRoles().toString())
				.setIssuer("Martial Kom")
				.setIssuedAt(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
	
	
	public boolean validateAccessToken(String token) {
		try {
			
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
			
		}catch (ExpiredJwtException ex1) {
			LOGGER.error("JWT expired", ex1);
		}catch(IllegalArgumentException ex2) {
			LOGGER.error("Token is null, empty or has only whitespace", ex2);
		}catch(MalformedJwtException ex3) {
			LOGGER.error("JWT is Invalid", ex3);
		}catch (UnsupportedJwtException ex4) {
			LOGGER.error("JWT is not supported",ex4);
		}catch(SignatureException ex5) {
			LOGGER.error("Signature validation failed", ex5);
		}
		return false;
	}
	
	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}
	
	public Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
	}
}
