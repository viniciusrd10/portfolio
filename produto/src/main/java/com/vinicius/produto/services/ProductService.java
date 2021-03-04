package com.vinicius.produto.services;

import com.vinicius.produto.controllers.ProductController;
import com.vinicius.produto.controllers.dtos.ProductDto;
import com.vinicius.produto.errors.ResourceInternalErrorServer;
import com.vinicius.produto.errors.ResourceNotFoundException;
import com.vinicius.produto.models.Product;
import com.vinicius.produto.repositories.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;

@Service
public class ProductService implements IProductService {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    private ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @NotNull
    public URI generateUri(UriComponentsBuilder uriComponentsBuilder, Product product) {
        var uri = uriComponentsBuilder.path("/product/{id}").buildAndExpand(product.getId()).toUri();
        return uri;
    }

    @Transactional
    public Product save(Product product) {

        if (product != null) {
            logger.info("Salvando o produto na base.");
            return repository.saveAndFlush(product);
        }
        logger.error("Erro ao salvar o produto na base.");
        throw new ResourceInternalErrorServer("Falha ao salvar o produto. Tente mais tarde!");
    }

    public Product findById(Long id) {
        logger.info("Buscando um produto por id: " + id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não econtrado!"));
    }

    public Page<ProductDto> findByFilters(String q, Double minPrice, Double maxPrice, Pageable pagination) {

        var products = repository.findByPriceBetweenAndNameOrDescription(minPrice, maxPrice, q, q, pagination);
        return ProductDto.converter(products);
    }

    public void deleteById(Product product) {
        if (product != null) {
            repository.deleteById(product.getId());
        }
    }

    public Page<ProductDto> findAll(Pageable pagination) {
        Page<ProductDto> productsList = ProductDto.converter(repository.findAll(pagination));
        return productsList;
    }
}
