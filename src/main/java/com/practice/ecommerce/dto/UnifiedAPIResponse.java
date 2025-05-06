package com.practice.ecommerce.dto;

import com.practice.ecommerce.exception.ErrorDetails;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnifiedAPIResponse<T> {
    private boolean success;
    private T data;
    private ErrorDetails error;
}
