package com.example.demo.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotEmpty(message = "Tên nhóm không được để trống")
    private String name;
}
