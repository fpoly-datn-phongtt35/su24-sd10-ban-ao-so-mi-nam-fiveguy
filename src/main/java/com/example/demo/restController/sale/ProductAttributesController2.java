package com.example.demo.restController.sale;


import com.example.demo.entity.*;
import com.example.demo.service.sale.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/infoProduct")
public class ProductAttributesController2 {

    @Autowired
    private WristService2 wristService2;

    @Autowired
    private SizeService2 sizeService2;

    @Autowired
    private MaterialService2 materialService2;

    @Autowired
    private ColorService2 colorService2;

    @Autowired
    private CollarService2 collarService2;

    @Autowired
    private BrandService2 brandService2;



    @GetMapping("/wrists")
    public List<Wrist> getAllWrists() {
        return wristService2.getAllWrists();
    }

    @GetMapping("/sizes")
    public List<Size> getAllSizes() {
        return sizeService2.getAllSizes();
    }

    @GetMapping("/materials")
    public List<Material> getAllMaterials() {
        return materialService2.getAllMaterials();
    }

    @GetMapping("/colors")
    public List<Color> getAllColors() {
        return colorService2.getAllColors();
    }

    @GetMapping("/collars")
    public List<Collar> getAllCollars() {
        return collarService2.getAllCollars();
    }

    @GetMapping("/brands")
    public List<Brand> getAllBrands() {
        return brandService2.getAllBrands();
    }

}
