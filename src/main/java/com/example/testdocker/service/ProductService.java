package com.example.testdocker.service;


import com.example.testdocker.domain.entity.Product;
import com.example.testdocker.domain.repository.ProductRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProductService {


    @Autowired
    private ProductRespository productRespository;


    // Create or Update Product (Index)
    @Transactional
    public String createOrUpdateProduct(Product product) throws IOException {
        //Lưu product vào db (postgres)
        productRespository.save(product);

        return "Create success";
    }

    // Update Product by ID

    @Transactional
    public String updateProduct(Product product, Long id) throws IOException {
        Product newProduct = productRespository.findById(id).orElse(null);
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setManufacturer(product.getManufacturer());
        newProduct.setQuantity(product.getQuantity());
        productRespository.save(newProduct);
        return "Update success";


    }


    // Delete Product by ID
    @Transactional
    public String deleteProduct(Long id) throws IOException {
        productRespository.deleteById(id);
        return "Delete success";
    }


}
