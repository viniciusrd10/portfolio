package com.vinicius.produto.controllers;

import com.vinicius.produto.controllers.dtos.ProductDto;
import com.vinicius.produto.controllers.forms.ProductForm;
import com.vinicius.produto.errors.ErrorResponse;
import com.vinicius.produto.models.Product;
import com.vinicius.produto.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@Api(tags = "Produtos", protocols = "http")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @PostMapping
    @CacheEvict(value = "listaDeProdutos", allEntries = true)
    @ApiOperation(value = "Criação de um produto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Produto cadastrado!", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Solicitação inválida!", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Acesso negado!"),
            @ApiResponse(code = 404, message = "Recurso não encontrado!"),
            @ApiResponse(code = 500, message = "Erro no servidor!")
    })
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductForm form, UriComponentsBuilder uriComponentsBuilder) {
        logger.info("Criandor um produto. Parâmentros de entrada: " + form);
        var product = service.save(form.converter());
        return ResponseEntity.created(service.generateUri(uriComponentsBuilder, product)).body(new ProductDto(product));
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Atualização de um produto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Produto cadastrado!", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Solicitação inválida!", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Acesso negado!"),
            @ApiResponse(code = 404, message = "Recurso não encontrado!"),
            @ApiResponse(code = 500, message = "Erro no servidor!")
    })
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductForm form) {
        logger.info("Atualizando um produto. Parâmentros de entrada: " + form + " id: " + id);

        Product product = form.converter();
        if (!service.findById(id).equals(product)) {
            product.setId(id);
            service.save(product);
        }
        return ResponseEntity.ok(new ProductDto(product));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Busca de um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Produto cadastrado!", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Solicitação inválida!", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Acesso negado!"),
            @ApiResponse(code = 404, message = "Recurso não encontrado!"),
            @ApiResponse(code = 500, message = "Erro no servidor!")
    })
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {

        return ResponseEntity.ok(new ProductDto(service.findById(id)));
    }

    @GetMapping
    @Cacheable(value = "listaDeProdutos")
    @ApiOperation(value = "Listadeprodutos")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Solicitação inválida!", response = List.class),
            @ApiResponse(code = 404, message = "Recurso não encontrado!"),
            @ApiResponse(code = 500, message = "Erro no servidor!")
    })
    public Page<ProductDto> productsList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.info("Listando todos os produtos. ");

        var products = service.findAll(pageable);
        return products;
    }

    @GetMapping("/search")
    @ApiOperation(value = "Lista de produtos filtrados")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Solicitação inválida!", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Recurso não encontrado!"),
            @ApiResponse(code = 500, message = "Erro no servidor!")
    })
    public Page<ProductDto> filteredProductsList(@RequestParam(required = false) String q, @RequestParam(required = false, name = "min_price") Double minPrice, @RequestParam(required = false, name = "max_price") Double maxPrice, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable paginacao) {
        logger.info("Listando todos os prodtos por filtro. Descrição ou nome: " + q + " Preço mínimo: " + minPrice + " Preço máximo: " + maxPrice);
        var products = service.findByFilters(q, minPrice, maxPrice, paginacao);
        return products;
    }

    @DeleteMapping(value = "/{id}")
    @CacheEvict(value = "listaDeProdutos", allEntries = true)
    @ApiOperation(value = "Deleção de um produto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Produto cadastrado!", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Solicitação inválida!", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Acesso negado!"),
            @ApiResponse(code = 404, message = "Recurso não encontrado!"),
            @ApiResponse(code = 500, message = "Erro no servidor!")
    })
    public ResponseEntity<ProductDto> deleteById(@PathVariable Long id) {
        logger.info("Deletando produto com id + %s: ", id);
        service.deleteById(service.findById(id));

        return ResponseEntity.ok().build();
    }
}
