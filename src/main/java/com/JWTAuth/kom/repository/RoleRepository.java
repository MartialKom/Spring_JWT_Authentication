package com.JWTAuth.kom.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.JWTAuth.kom.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

	
}
