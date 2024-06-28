package com.example.demo.model.response.nguyen;


import com.example.demo.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductFilterResponse {

    private List<Category> categories;
    private List<Material> materials;
    private List<Collar> collars;
    private List<Wrist> wrists;
    private List<Color> colors;
    private List<Size> sizes;
}
