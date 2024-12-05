package com.example.testdocker.controller;


import com.example.testdocker.domain.entity.Order;
import com.example.testdocker.domain.entity.Product;
import com.example.testdocker.domain.request.OrderRequest;
import com.example.testdocker.service.OrderService;
import com.example.testdocker.service.elasticsearch.OrderElsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @Autowired
    private OrderElsService orderElsService;

    @GetMapping()
    public List<Order> getAllProducts() throws IOException {
        return orderElsService.getAllOrder();
    }

    // Get Order by ID
    @GetMapping("/{id}")
    public Order getProductById(@PathVariable String id) throws IOException {
        return orderElsService.getOrderById(id);
    }

    @PostMapping
    public String createOrUpdateProduct(@RequestBody OrderRequest orderRequest) throws IOException {
        return orderService.createOrder(orderRequest);
    }


    // Update Product
    @PutMapping("/{id}")
    public String updateProduct(@RequestBody OrderRequest  orderRequest, @PathVariable Long id)  {
        return orderService.updateOrder(orderRequest,id);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) throws IOException {
        return orderService.deleteOrder(id);
    }
}
