package com.netagentciadigital.api.service;

import com.netagentciadigital.api.commons.exceptions.DataNotFoundException;
import com.netagentciadigital.api.model.Product;
import com.netagentciadigital.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product findById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new DataNotFoundException("Product with id '" + id + "' not found!");
        }

        return product.get();
    }

    public List<Product> filter(String name) {
        return productRepository.findByNameLike(name);
    }

    public List<Product> create(List<Product> products) {
        for(Product product: products){
            product.setId(null);
        }

        return productRepository.saveAll(products);
    }

    public Product update(String id, Product product) {
        Product productOld = findById(id);

        product.setId(productOld.getId());

        return productRepository.save(product);
    }

    public Product delete(String id) {
        Product product = findById(id);
        productRepository.deleteById(id);
        return product;
    }

}
