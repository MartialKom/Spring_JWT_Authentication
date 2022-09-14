package com.JWTAuth.kom.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.JWTAuth.kom.model.Produit;
@Repository
public interface ProduitRepository extends CrudRepository<Produit, Integer> {

	
	
}
