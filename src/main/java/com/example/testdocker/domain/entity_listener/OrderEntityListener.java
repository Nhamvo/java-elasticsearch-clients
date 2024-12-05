package com.example.testdocker.domain.entity_listener;

import com.example.testdocker.domain.entity.Order;
import com.example.testdocker.domain.entity.Product;
import com.example.testdocker.service.elasticsearch.OrderElsService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class OrderEntityListener {
    @Autowired
    OrderElsService orderElsService;


    // @PostPersist được gọi sau khi dữ liệu được persist (insert vào DB)
    @PostPersist
    public void onProductCreated(Order order) throws IOException {
        // Cập nhật dữ liệu vào Elasticsearch sau khi tạo mới
        orderElsService.indexOrder(order);
    }

    // @PostUpdate được gọi sau khi dữ liệu được cập nhật trong DB
    @PostUpdate
    public void onProductUpdated(Order order) throws IOException {
         // Cập nhật dữ liệu vào Elasticsearch sau khi cập nhật
        orderElsService.updateOrder(order);
    }

    // @PostRemove được gọi sau khi dữ liệu bị xóa trong DB
    @PostRemove
    public void onProductDeleted(Order order) throws IOException {
         // Xóa dữ liệu khỏi Elasticsearch khi bị xóa trong DB
        orderElsService.deleteOrder(order);
    }
}
