package com.netagentciadigital.api.controller;

import com.netagentciadigital.api.model.Product;
import com.netagentciadigital.api.model.response.ApiResponseBody;
import com.netagentciadigital.api.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/products")
public class ProductController {


    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseBody> getAll(@RequestParam(value = "name", required = false) String name){
        ApiResponseBody result = ApiResponseBody.builder()
                .status("200")
                .result(productService.filter(name))
            .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseBody> findById(@PathVariable("id") String id){
        log.info(id);
        ApiResponseBody result = ApiResponseBody.builder()
                .status("200")
                .result(productService.findById(id))
            .build();
        result.put("productID",id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseBody> create(@RequestBody List<Product> products){
        ApiResponseBody result = ApiResponseBody.builder()
                .status("200")
                .result(productService.create(products))
            .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseBody> update(@PathVariable("id") String id, @RequestBody Product product){
        ApiResponseBody result = ApiResponseBody.builder()
                .status("200")
                .result(productService.update(id, product))
            .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseBody> delete(@PathVariable("id") String id){
        ApiResponseBody result = ApiResponseBody.builder()
                .status("200")
                .result(productService.delete(id))
            .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
