package com.example.testdocker.controller;


import com.example.testdocker.entity.Product;
import com.example.testdocker.service.ElasticsearchService;
import com.example.testdocker.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    // Create or Update Product

    @GetMapping()
    public List<Product> getAllProducts() throws IOException {
        return elasticsearchService.getAllProducts();
    }

    @PostMapping
    public String createOrUpdateProduct(@RequestBody Product product) throws IOException {
        return productService.createOrUpdateProduct(product);
    }

    // Get Product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) throws IOException {
        return elasticsearchService.getProductById(id);
    }

    // Update Product
    @PutMapping("/{id}")
    public String updateProduct(@RequestBody Product product,@PathVariable Long id) throws IOException {
        return productService.updateProduct(product,id);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) throws IOException {
        return productService.deleteProduct(id);
    }

    // Search Products
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String query) throws IOException {
        return elasticsearchService.searchProducts(query);
    }

    @GetMapping("/filter")
    public List<Product> multiInstanceSearchProducts(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) Integer quantity,
                                                     @RequestParam(required = false) String manufacturer,
                                                     @RequestParam(required = false) Double minPrice,
                                                     @RequestParam(required = false) Double maxPrice) throws IOException {
        return elasticsearchService.multiInstanceSearchProducts(name, quantity, manufacturer, minPrice, maxPrice);
    }
}
