package com.vinicius.produto.repositories;

import com.vinicius.produto.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM PRODUTO p WHERE p.price BETWEEN :minPrice and :maxPrice and p.name = :name or p.description = :description")
    Page<Product> findByPriceBetweenAndNameOrDescription(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("name")String name, @Param("description") String description, Pageable pagination);
}
