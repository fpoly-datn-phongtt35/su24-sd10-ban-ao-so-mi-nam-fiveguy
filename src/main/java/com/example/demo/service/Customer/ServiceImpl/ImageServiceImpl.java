package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Image;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.Customer.ImageRepostitory;
import com.example.demo.service.Customer.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class    ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepostitory imageRepository;

    @Override
    public List<Image> getAll() {
        return imageRepository.findAll();
    }

    @Override
    public Page<Image> getAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return imageRepository.findAll(pageable);
    }

    @Override
    public Image getById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    @Override
    public Image save(Image imageReq) {
        imageReq.setStatus(1);
        imageReq.setCreatedAt(new Date());
        imageReq.setUpdatedAt(new Date());
        return imageRepository.save(imageReq);
    }

    @Override
    public List<Image> saveAll(List<Image> images, ProductDetail productDetail) {
        System.out.println("cc");
        System.out.println(images);
        if(images.isEmpty()){
            return null;
        }
        for (Image i: images) {
            i.setStatus(1);
            i.setCreatedAt(new Date());
            i.setUpdatedAt(new Date());
//            i.setProductDetail(productDetail);
//            i.setName(productDetail.getId()+"");
        }
        return imageRepository.saveAll(images);
    }

    @Override
    public Image update(Image imageReq, Long id) {
        Optional<Image> imageOptional = imageRepository.findById(id);
        System.out.println(imageOptional);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            image.setName(imageReq.getName());
            image.setPath(imageReq.getPath());
//            image.setProductDetail(imageReq.getProductDetail());
            image.setUpdatedAt(new Date());
            image.setStatus(imageReq.getStatus());
            System.out.println(image);
            return imageRepository.save(image);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
        } else {
            System.err.println("Error delete ImageserviceImpl");
        }
    }

    @Override
    public List<Image> getByPDid(Long id) {
        return imageRepository.findAllByProductDetailId(id);
    }



}
