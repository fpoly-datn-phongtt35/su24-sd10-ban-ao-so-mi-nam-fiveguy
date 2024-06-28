package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.*;
import com.example.demo.model.response.nguyen.ProductFilterResponse;
import com.example.demo.repository.nguyen.product.*;
import com.example.demo.service.nguyen.NProductPropertySerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NProductPropertyServiceImpl implements NProductPropertySerivce {

    @Autowired
    NProductRepository productRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;

    @Autowired
    NCategoryRepository categoryRepository;

    @Autowired
    NMaterialRepository materialRepository;

    @Autowired
    NCollarRepository collarRepository;

    @Autowired
    NWristRepository wristRepository;

    @Autowired
    NColorRepository colorRepository;

    @Autowired
    NSizeRepository sizeRepository;

    @Override
    public ProductFilterResponse getAllProductFilterProperty() {

        ProductFilterResponse productFilterResponse = new ProductFilterResponse();

        List<Category> categories = categoryRepository.findAllByStatusOrderByCreatedAtDesc(1);
        List<Material> materials = materialRepository.findAllByStatusOrderByCreatedAtDesc(1);
        List<Collar> collars = collarRepository.findAllByStatusOrderByCreatedAtDesc(1);
        List<Wrist>  wrists = wristRepository.findAllByStatusOrderByCreatedAtDesc(1);
        List<Color> colors = colorRepository.findAllByStatusOrderByCreatedAtDesc(1);
        List<Size> sizes = sizeRepository.findAllByStatusOrderByCreatedAtDesc(1);

        productFilterResponse.setCategories(categories);
        productFilterResponse.setMaterials(materials);
        productFilterResponse.setCollars(collars);
        productFilterResponse.setWrists(wrists);
        productFilterResponse.setColors(colors);
        productFilterResponse.setSizes(sizes);

        return productFilterResponse;
    }

}
