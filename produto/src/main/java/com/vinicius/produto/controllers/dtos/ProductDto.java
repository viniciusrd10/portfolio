package com.vinicius.produto.controllers.dtos;

import com.vinicius.produto.models.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Double price;


    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public static Page<ProductDto> converter(Page<Product> products) {
        return products.map(ProductDto::new);
    }


}
