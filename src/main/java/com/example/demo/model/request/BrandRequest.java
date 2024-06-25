package com.example.demo.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandRequest {
    @NotEmpty(message = "Tên thương hiệu không được để trống")
    private String name;
}
