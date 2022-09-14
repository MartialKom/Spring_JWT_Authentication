package com.JWTAuth.kom.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.JWTAuth.kom.model.Role;
import com.JWTAuth.kom.model.User;
import com.JWTAuth.kom.repository.UserRepository;



@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	
	@Autowired UserRepository userService;
	
	
	@Test
	public void testCreateUser() {
		PasswordEncoder passEncoder = new BCryptPasswordEncoder();
		String rawPassword = "secret";
		String encodedPass = passEncoder.encode(rawPassword);
		User newUser = new User("ledouxtagne@gmail.com", encodedPass);
		
		
		User savedUser = userService.save(newUser);
		
		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testAsignRole() {
		int userID = 2;
		Integer roleID = 1;
		
		User user = userService.findById(userID).get();
		//user.addRole(new Role(roleID));
		user.addRole(new Role(2));
		User updateUser = userService.save(user);
		
		assertThat(updateUser.getRoles()).hasSize(1);
	}
}
