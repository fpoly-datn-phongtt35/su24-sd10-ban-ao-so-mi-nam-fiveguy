package com.example.demo.model.request.thuong;

import com.example.demo.entity.Brand;
import com.example.demo.entity.BrandSuppiler;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SupplierRequestTH {
    @NotEmpty(message = "Tên nhà cung cấp không được để trống")
    private String name;

    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;

    @NotEmpty(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @Email(message = "Gmail không hợp lệ")
    private String email;

    @NotEmpty(message = "Danh sách thương hiệu không được để trống")
    private Set<Brand> brands;

    private Set<BrandSuppiler> brandSuppilers;
}
