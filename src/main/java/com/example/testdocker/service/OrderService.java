package com.example.testdocker.service;

import com.example.testdocker.domain.entity.Order;
import com.example.testdocker.domain.entity.Product;
import com.example.testdocker.domain.repository.OrderRepository;
import com.example.testdocker.domain.repository.ProductRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.testdocker.domain.request.OrderRequest;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRespository productRespository;

    @Transactional
    public String createOrder(OrderRequest orderRequest) {
        Order newOrder = new Order();
        newOrder.setQuantity(orderRequest.getQuantity());
        newOrder.setUserName(orderRequest.getUserName());
        List<Product> listProduct = productRespository.findAllById(orderRequest.getProductId());
        newOrder.setProduct(listProduct);
        orderRepository.save(newOrder);
        return "Order created";
    }

    @Transactional
    public String updateOrder(OrderRequest orderRequest, Long id) {
        Order newOrder = orderRepository.findById(id).orElse(null);
        newOrder.setQuantity(orderRequest.getQuantity());
        newOrder.setUserName(orderRequest.getUserName());
        List<Product> listProduct = productRespository.findAllById(orderRequest.getProductId());
        newOrder.setProduct(listProduct);
        orderRepository.save(newOrder);
        return "Order updated";
    }

    @Transactional
    public String deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return "Order deleted";
    }

}
