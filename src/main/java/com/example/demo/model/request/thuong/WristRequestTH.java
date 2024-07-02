package com.example.demo.model.request.thuong;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WristRequestTH {
    @NotEmpty(message = "Tên cổ tay không được để trống")
    private String name;
}
