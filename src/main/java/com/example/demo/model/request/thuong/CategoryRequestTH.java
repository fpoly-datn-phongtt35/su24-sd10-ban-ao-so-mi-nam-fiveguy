package com.example.demo.model.request.thuong;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestTH {
    @NotEmpty(message = "Tên nhóm không được để trống")
    private String name;
}
