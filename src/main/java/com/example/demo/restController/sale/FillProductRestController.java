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
@RequestMapping("/api/admin/infoProduct")
public class FillProductRestController {

    @Autowired
    private WristService wristService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private CollarService collarService;

    @Autowired
    private BrandService brandService;



    @GetMapping("/wrists")
    public List<Wrist> getAllWrists() {
        return wristService.getAllWrists();
    }

    @GetMapping("/sizes")
    public List<Size> getAllSizes() {
        return sizeService.getAllSizes();
    }

    @GetMapping("/materials")
    public List<Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/colors")
    public List<Color> getAllColors() {
        return colorService.getAllColors();
    }

    @GetMapping("/collars")
    public List<Collar> getAllCollars() {
        return collarService.getAllCollars();
    }

    @GetMapping("/brands")
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }

}
