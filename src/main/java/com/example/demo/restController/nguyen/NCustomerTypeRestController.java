package com.example.demo.restController.nguyen;

import com.example.demo.service.nguyen.NCustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customerTypeN")
public class NCustomerTypeRestController {

    @Autowired
    NCustomerTypeService customerTypeService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(customerTypeService.getAllCustomerType());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(customerTypeService.getCustomerTypeById(id));
    }

}
