package com.darpan.Ecommerce_Application.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import com.darpan.Ecommerce_Application.model.Product;

import java.sql.Connection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {



    @Query("SELECT p FROM Product p WHERE "+
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "+
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "+
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "+
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProduct(String keyword);
}
