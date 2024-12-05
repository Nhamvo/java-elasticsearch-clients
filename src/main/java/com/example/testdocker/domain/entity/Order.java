package com.example.testdocker.domain.entity;


import com.example.testdocker.domain.entity_listener.OrderEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")

@EntityListeners(OrderEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName ;
    private int quantity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id") // Thêm khóa ngoại trực tiếp vào bảng Product
    private List<Product> product;
}
