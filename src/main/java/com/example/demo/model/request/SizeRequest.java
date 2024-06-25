package com.example.demo.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeRequest {
    @NotEmpty(message = "Tên size không được để trống")
    private String name;
}
