package com.vinicius.produto.services;

import com.vinicius.produto.controllers.dtos.ProductDto;
import com.vinicius.produto.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public interface IProductService {

    URI generateUri(UriComponentsBuilder uriComponentsBuilder, Product product);
    Product save(Product product);
    Product findById(Long id);
    Page<ProductDto> findByFilters(String q, Double minPrice, Double maxPrice, Pageable pagination);
    void deleteById(Product product);
    Page<ProductDto> findAll(Pageable pagination);

}
