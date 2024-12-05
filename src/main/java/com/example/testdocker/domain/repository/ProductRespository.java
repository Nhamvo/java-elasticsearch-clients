package com.example.testdocker.domain.repository;

import com.example.testdocker.domain.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRespository extends JpaRepository<Product, Long> {
    // Fuzzy Query
//    @Query("{\"fuzzy\": {\"name\": {\"value\": \"?0\"}}}")
//    List<Product> findByNameFuzzy(String name);
//
//    // Bool Query
//    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}, {\"range\": {\"age\": {\"lte\": \"?1\"}}}]}}")
//    List<Product> findByNameAndAgeLessThanEqual(String name, Integer maxAge);

}
