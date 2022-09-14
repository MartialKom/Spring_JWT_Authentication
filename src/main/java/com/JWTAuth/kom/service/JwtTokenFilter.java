package com.JWTAuth.kom.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.JWTAuth.kom.model.JwtTokenUtil;
import com.JWTAuth.kom.model.Role;
import com.JWTAuth.kom.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired private JwtTokenUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if(!hasAuthorizationHeader(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String accessToken = getAcessToken(request);
		
		if(!jwtUtil.validateAccessToken(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		setAuthenticationContext(accessToken, request);
		filterChain.doFilter(request, response);


	}
	
private void  setAuthenticationContext(String accessToken, HttpServletRequest request) 
{
	UserDetails userDetails = getUserDetails(accessToken);
	
	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	
	SecurityContextHolder.getContext().setAuthentication(authentication);
}

	private UserDetails getUserDetails(String accessToken) {
	User userDetail = new User();
	Claims claims = jwtUtil.parseClaims(accessToken); //On récupere en réalité le role de l'user dans le token
	
	String claimRoles = (String) claims.get("roles");
	
	System.out.println("claimRoles: "+claimRoles);
	
	claimRoles = claimRoles.replace("[", "").replace("]", "");
	String[] roleNames = claimRoles.split(",");
	
	for(String aRolename: roleNames) {
		userDetail.addRole(new Role(aRolename));
	}
	
	String subject = (String) claims.get(Claims.SUBJECT);
	String[] subjectArray = subject.split(",");
	
	userDetail.setId(Integer.parseInt(subjectArray[0]));
	userDetail.setEmail(subjectArray[1]);
	
	return userDetail;
}

	//Verifier si l'en tete correspond à ce qu'on attend
private boolean hasAuthorizationHeader(HttpServletRequest request) {
	
	String header = request.getHeader("Authorization");
	System.out.println("Authorization header: "+header);
	
	if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
		return false;
	}
	return true;
}
	
private String getAcessToken(HttpServletRequest request) {
	String header = request.getHeader("Authorization");
	String token = header.split(" ")[1].trim();
	System.out.println("Acess Token: "+token);
	return token;
	
}
}
