package com.vinicius.produto.services;

import com.vinicius.produto.models.Product;
import com.vinicius.produto.repositories.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {


    private IProductService service;

    @MockBean
    private ProductRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new ProductService(repository);
    }

    @Test
    @DisplayName("Deve salvar o produto na base de dados")
    void saveProduct(){
        var product = new Product().builder()
                .name("lapiseira")
                .description("Lapiseira pentel 0.7mm")
                .price(1.25)
                .expirationDate(null)
                .build();

        var productResult = new Product().builder()
                .id(14l)
                .name("lapiseira")
                .description("Lapiseira pentel 0.7mm")
                .price(1.25)
                .expirationDate(null)
                .build();

        Mockito.when(repository.saveAndFlush(Mockito.any())).thenReturn(productResult);

        var saved = service.save(product);

        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getId()).isEqualTo(14l);

    }
}
