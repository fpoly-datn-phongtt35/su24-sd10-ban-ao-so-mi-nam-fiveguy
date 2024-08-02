package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Image;
import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.thuong.ProductDetailResponseTH;
import com.example.demo.repository.thuong.ImageRepositoryTH;
import com.example.demo.repository.thuong.ProductDetailRepositoryTH;
import com.example.demo.service.thuong.ProductDetailServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDetailServiceTHImpl implements ProductDetailServiceTH {
    @Autowired
    private ProductDetailRepositoryTH repository;

    @Autowired
    private ImageRepositoryTH imageRepository;

    @Override
    public List<ProductDetail> findAllByProduct_Id(Long id) {
        return repository.findAllByProduct_Id(id);
    }

    public String getImagePathByProductId(Long id, Long colorId) {
        List<Image> images = imageRepository.findAllByProductIdAndColorIdAndStatus(id, colorId, 1);
        return images.isEmpty() ? null : images.get(0).getPath();
    }

    @Override
    public List<ProductDetailResponseTH> getAll(String keyword) {
        List<ProductDetail> productDT = repository.findAllByStatus(keyword);
        List<ProductDetailResponseTH> listProductDT = new ArrayList<>();
        for (ProductDetail pd : productDT) {
            ProductDetailResponseTH pdr = new ProductDetailResponseTH();
            pdr.setId(pd.getId());
            pdr.setQuantity(pd.getQuantity());
            pdr.setBarcode(pd.getBarcode());
            pdr.setCreatedAt(pd.getCreatedAt());
            pdr.setUpdatedAt(pd.getUpdatedAt());
            pdr.setCreatedBy(pd.getCreatedBy());
            pdr.setUpdatedBy(pd.getUpdatedBy());
            pdr.setStatus(pd.getStatus());
            pdr.setProduct(pd.getProduct());
            pdr.setSize(pd.getSize());
            pdr.setColor(pd.getColor());

            // Gán imagePath bằng hàm getImagePathByProductId
            String imagePath = getImagePathByProductId(pd.getProduct().getId(), pd.getColor().getId());
            pdr.setImagePath(imagePath);

            listProductDT.add(pdr);
        }


        return listProductDT;
    }

    @Override
    public ProductDetail findById(Long id) {
        ProductDetail pd = repository.findById(id).orElse(null);
        if (pd != null) {
            if (pd.getStatus() == 0) {
                throw new DuplicateException("Sản phẩm " + pd.getProduct().getName() + " đã ngừng kinh doanh", "alert");
            } else if (pd.getQuantity() < 1 ) {
                throw new DuplicateException("Không đủ số lượng tồn kho cho sản phẩm " + pd.getProduct().getName(), "alert");
            }
        }
        return pd;
    }
}
