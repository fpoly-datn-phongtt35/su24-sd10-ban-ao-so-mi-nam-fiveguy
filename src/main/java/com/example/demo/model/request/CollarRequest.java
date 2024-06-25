package com.example.demo.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollarRequest {
    @NotEmpty(message = "Tên cổ áo không được để trống")
    private String name;
}
