package com.example.demo.model.request.thuong;

import com.example.demo.entity.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProductRequestTH {
    @NotEmpty(message = "Mã code không được để trống")
    private String code;

    @NotEmpty(message = "Tên không được để trống")
    private String name;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "500.00", message = "Giá phải lớn hơn hoặc bằng 500 VNĐ")
    private BigDecimal price;

    private String describe;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private Integer status;

    @NotNull(message = "Nhà cung cấp không được để trống")
    private Supplier supplier;

    @NotNull(message = "Chất liệu không được để trống")
    private Material material;

    @NotNull(message = "Cổ tay không được để trống")
    private Wrist wrist;

    @NotNull(message = "Cổ áo không được để trống")
    private Collar collar;

    @NotNull(message = "Nhóm sản phẩm không được để trống")
    private Category category;

    private List<Image> images;

    private List<ProductDetail> productDetails;
}
