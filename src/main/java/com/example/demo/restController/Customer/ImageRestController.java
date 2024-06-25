package com.example.demo.restController.Customer;

import com.example.demo.entity.Image;
import com.example.demo.service.Customer.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@CrossOrigin("*")
@RequestMapping("/api/image")
public class ImageRestController {
//
//    @Autowired
//    ImageService imageService;
//
//    @GetMapping("")
//    public ResponseEntity<?> index(){
//        System.out.println("image");
//        List<Image> images = imageService.getAll();
//        return ResponseEntity.ok(images);
//    }
//
//    @GetMapping("/page")
//    public ResponseEntity<?> page(@RequestParam(value = "page", defaultValue = "0") Integer page){
//        System.out.println("image");
//        Page<Image> images = imageService.getAll(page);
//        return ResponseEntity.ok(images);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getById(@PathVariable("id") Long id){
//        System.out.println("image");
//        Image image = imageService.getById(id);
//        System.out.println(image);
//        return ResponseEntity.ok(image);
//    }
//
//    @PostMapping("")
//    public ResponseEntity<?> add(@RequestBody Image imageReq){
//        Image image = imageService.save(imageReq);
//        return ResponseEntity.ok(image);
//    }
//
////    @PostMapping("/saveAll")
////    public ResponseEntity<?> addAll(@RequestBody List<Image> imagesReq){
////        List<Image> image = imageService.saveAll(imagesReq);
////        return ResponseEntity.ok(image);
////    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Image imageReq){
//        System.out.println("aaaa"+imageReq);
//        Image image = imageService.update(imageReq, id);
//        return ResponseEntity.ok(image);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable("id") Long id){
//        imageService.delete(id);
//    }
//
//    @GetMapping("/pd/{id}")
//    public ResponseEntity<?> getByPDid(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(imageService.getByPDid(id));
//    }
}
