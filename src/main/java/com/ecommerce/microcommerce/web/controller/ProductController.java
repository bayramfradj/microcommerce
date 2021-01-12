package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.Dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Api( description="API pour es opérations CRUD sur les produits.")
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @ApiOperation(value = "tous les produit")
    @RequestMapping(value = "/product",method = RequestMethod.GET)
    public List<Product> ListProduit()
    {
        return productDao.findAll();
    }

    @ApiOperation(value = "produit par id")
    @GetMapping(value = "/product/{id}")
    public Product afficherProduit(@PathVariable int id)
    {
        Product p = productDao.findById(id);
        if(p==null)
            throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE.");

        return p;
    }

    @ApiOperation(value = "tous les produit superieur a prix donnee")
    @GetMapping(value = "/product/PrixGreaterThan/{prix}")
    public List<Product> GreaterThan(@PathVariable Double prix)
    {
        return productDao.findByPrixGreaterThan(prix);
    }

    @ApiOperation(value = "tous les produit contiant nom donnee")
    @GetMapping(value = "/product/LikeNom/{nom}")
    public List<Product> LikeNom(@PathVariable String nom)
    {
        return productDao.findByNomLike("%"+nom+"%");
    }

    @ApiOperation(value = "ajouter produit")
    @PostMapping(value = "/product")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product)
    {
        Product p = productDao.save(product);
        if(p == null)
        return  ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(p.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "supprimer produit par id")
    @DeleteMapping(value = "/product/{id}")
    public void supprimerProduit(@PathVariable int id) {
        productDao.deleteById(id);
    }

    @ApiOperation(value = "Modifier produit")
    @PutMapping(value = "/product")
    public void updateProduit(@Valid @RequestBody Product product)
    {

        productDao.save(product);
    }
}
