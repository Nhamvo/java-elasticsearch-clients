package com.example.testdocker.service;


import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import co.elastic.clients.json.JsonData;
import com.example.testdocker.entity.Product;
import com.example.testdocker.repository.ProductRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.ElasticsearchClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
