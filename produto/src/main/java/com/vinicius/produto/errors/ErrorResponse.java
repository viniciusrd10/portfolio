package com.vinicius.produto.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({"statusCode", "message"})
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("message")
    private String message;
}
