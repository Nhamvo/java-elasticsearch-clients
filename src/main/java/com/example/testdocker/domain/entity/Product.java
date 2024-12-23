package com.example.testdocker.domain.entity;

//import jakarta.persistence.*;
import com.example.testdocker.domain.entity_listener.ProductEntityListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")

@EntityListeners(ProductEntityListener.class) // Liên kết với Entity Listener
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer quantity;
    private String manufacturer;
    private double price;
}
