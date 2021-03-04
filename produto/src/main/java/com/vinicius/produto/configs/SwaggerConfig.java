package com.vinicius.produto.configs;

import com.vinicius.produto.models.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket productApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vinicius.produto"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .ignoredParameterTypes(Product.class)
                .ignoredParameterTypes(Pageable.class)
                .ignoredParameterTypes(Sort.class)
                .ignoredParameterTypes(Page.class)
                .genericModelSubstitutes(ResponseEntity.class).useDefaultResponseMessages(false);
    }
}
