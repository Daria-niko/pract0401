package com.example.project2.service;

import com.example.project2.model.ProductModel;
import com.example.project2.repository.ProductRepository;
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

    public List<ProductModel> findAll() {
        return productRepository.findAll();
    }

    public Optional<ProductModel> findById(Long id) {
        return productRepository.findById(id);
    }

    public ProductModel save(ProductModel productModel) {
        return productRepository.save(productModel);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
