package com.example.testdocker.entity;

import com.example.testdocker.service.ElasticsearchService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ProductEntityListener {

    @Autowired
    private ElasticsearchService elasticsearchService; // Service thực hiện cập nhật vào Elasticsearch

    // @PostPersist được gọi sau khi dữ liệu được persist (insert vào DB)
    @PostPersist
    public void onProductCreated(Product product) throws IOException {
        // Cập nhật dữ liệu vào Elasticsearch sau khi tạo mới
        elasticsearchService.indexProduct(product);
    }

    // @PostUpdate được gọi sau khi dữ liệu được cập nhật trong DB
    @PostUpdate
    public void onProductUpdated(Product product) throws IOException {
        System.out.println("Product updated: " + product.getName());
        // Cập nhật dữ liệu vào Elasticsearch sau khi cập nhật
        elasticsearchService.updateProduct(product);
    }

    // @PostRemove được gọi sau khi dữ liệu bị xóa trong DB
    @PostRemove
    public void onProductDeleted(Product product) throws IOException {
        System.out.println("Product deleted: " + product.getName());
        // Xóa dữ liệu khỏi Elasticsearch khi bị xóa trong DB
        elasticsearchService.deleteProduct(product);
    }
}
