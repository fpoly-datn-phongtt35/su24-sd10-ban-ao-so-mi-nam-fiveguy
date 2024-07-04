package com.example.demo.model.request.thuong;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeRequestTH {
    @NotEmpty(message = "Tên size không được để trống")
    private String name;
}
