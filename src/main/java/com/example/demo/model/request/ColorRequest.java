package com.example.demo.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorRequest {
    @NotEmpty(message = "Tên màu không được để trống")
    private String name;

    @NotEmpty(message = "Mã màu không được để trống")
    private String colorCode;
}
