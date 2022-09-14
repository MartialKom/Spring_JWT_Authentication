package com.JWTAuth.kom.Controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.JWTAuth.kom.model.Produit;
import com.JWTAuth.kom.service.ProduitService;

@RestController
@RequestMapping(path = "produit/api/v1/", name = "API_Produits")
public class ProduitAPI {

	@Autowired
	ProduitService produitservice;

	
	@GetMapping
	@RolesAllowed({"ROLE_CUSTOMER", "ROLE_EDITOR", "ROLE_ADMIN"})
	public Iterable<Produit> getproduits() {
		
		return produitservice.getAllproduits();
	}
	
	@PostMapping
	@RolesAllowed("ROLE_EDITOR")
	@ResponseStatus(HttpStatus.CREATED)
	public Produit addProduit(@RequestBody @Valid Produit prod) {
		return produitservice.SetProduit(prod);
	}
	
}
