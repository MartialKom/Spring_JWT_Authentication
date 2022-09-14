package com.JWTAuth.kom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.JWTAuth.kom.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	@Query("from User u where u.email=?1")
	public Optional<User>findByEmail(String email);
	
}
