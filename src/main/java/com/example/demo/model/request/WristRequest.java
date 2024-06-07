package com.example.demo.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WristRequest {
    @NotEmpty(message = "Tên cổ tay không được để trống")
    private String name;
}
