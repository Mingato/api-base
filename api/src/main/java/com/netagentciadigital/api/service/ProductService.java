package com.netagentciadigital.api.service;

import com.netagentciadigital.api.commons.exceptions.DataNotFoundException;
import com.netagentciadigital.api.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    //TODO:private final ProductRepository productRepository;



    public Product findById(String id) {
        //TODO:get produc from repository
        Optional<Product> product = Optional.of(Product.builder().id(id).build());
        //Optional<Product> product = Optional.empty();
        if(product.isEmpty()){
            throw new DataNotFoundException("Product with id '" + id + "' not found!");
        }

        return product.get();
    }

    public List<Product> filter(String name) {
        return new ArrayList<>();
    }

    public List<Product> create(List<Product> products) {
        for(Product product: products){
            product.setId(null);
        }
        return products;
    }

    public Product update(String id, Product product) {
        Product productOld = findById(id);
        //TODO:update
        return productOld;
    }

    public Product delete(String id) {
        Product product = findById(id);
        //TODO:delete
        return product;
    }
}
