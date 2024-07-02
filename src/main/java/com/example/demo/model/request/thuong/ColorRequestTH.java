package com.example.demo.model.request.thuong;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorRequestTH {
    @NotEmpty(message = "Tên màu không được để trống")
    private String name;

    @NotEmpty(message = "Mã màu không được để trống")
    private String colorCode;
}
