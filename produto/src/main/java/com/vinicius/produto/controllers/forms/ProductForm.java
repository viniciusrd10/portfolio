package com.vinicius.produto.controllers.forms;

import com.vinicius.produto.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {

    @NotBlank (message = "O campo 'name' não pode ser vazio.")
    private String name;
    @NotBlank(message = "O campo 'description' não pode ser vazio.")
    private String description;
    @NotNull(message = "O campo 'price' não pode ser nulo.")
    @Min(value = 0)
    private Double price;

    public ProductForm(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public Product converter() {
        return new Product(name, description, price);
    }
}
