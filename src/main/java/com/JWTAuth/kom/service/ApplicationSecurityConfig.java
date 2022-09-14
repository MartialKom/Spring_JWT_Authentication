package com.JWTAuth.kom.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.JWTAuth.kom.repository.UserRepository;

/*Cette classe va nous permettre de gerer la configuration de base
pour gerer les acces à l'API, car par défaut toutes les requetes
ne sont pas autorisées grace à Spring security
*/

@SuppressWarnings("deprecation")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = false, securedEnabled = false, jsr250Enabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired private UserRepository userrepo;
	@Autowired private JwtTokenFilter jwtTokenFilter;
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	} 
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> userrepo.findByEmail(username)
				.orElseThrow(()-> new UsernameNotFoundException("user "+ username+" not found") )
				);
	}



	@Override @Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//Elements de base pour la sécutité de l'application
		
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//http.authorizeHttpRequests().anyRequest().permitAll(); //Accorder la permission à toutes les requetes
		
		
		//Changer le type d'erreur qui apparait lorsque l'accès est bloqué
		http.exceptionHandling().authenticationEntryPoint(
				(request, response, ex)->{
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
							ex.getMessage());
				}
				);
		
		//seulement les requetes vers l'URL précisée sont autorisées à tous
		http.authorizeHttpRequests()
		.antMatchers("/auth/login").permitAll()
		.anyRequest().authenticated();
		
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
