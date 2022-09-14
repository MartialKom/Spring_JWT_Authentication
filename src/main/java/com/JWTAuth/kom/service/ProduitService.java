package com.JWTAuth.kom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JWTAuth.kom.model.Produit;
import com.JWTAuth.kom.repository.ProduitRepository;

@Service
public class ProduitService {

	@Autowired
	ProduitRepository produitrepo;
	
	public Iterable<Produit> getAllproduits(){
		
		return produitrepo.findAll();
	}
	
	public Produit SetProduit(Produit prod) {
		
		Produit savedProduit = produitrepo.save(prod);
		return savedProduit;
	}
	
}
