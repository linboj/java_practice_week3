package com.practice.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.practice.ecommerce.exception.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnifiedAPIResponse<T> {
    private boolean success;
    private T data;
    private ErrorDetails error;
}
