package com.example.demo.restController.Customer;

import com.example.demo.entity.Address;
import com.example.demo.service.Customer.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/address")
@CrossOrigin("*")

public class AddressRestController {

    @Autowired
    AddressService addressService;

    @GetMapping("")
    public ResponseEntity<List<Address>> getAllAddress() {
        List<Address> address = addressService.getAllAddress();
        return ResponseEntity.ok(address);
    }

    @GetMapping("/timkiem-status/{st}")
    public ResponseEntity<?> getSStatus(@PathVariable Integer st){
        return ResponseEntity.ok(addressService.getSStatus(st));
    }

    @RequestMapping("/get-by-customer/{id}")
    public ResponseEntity<List<Address>> getAllByCustomerId(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findByCustomerId(id));
    }

//    @GetMapping("/pageall")
//    public ResponseEntity<Page<Address>> getAllAddressPage(@RequestParam(defaultValue = "0", name = "page") Integer page) {
//        return ResponseEntity.ok(addressService.getAllAddressPage(page));
//    }
//
//    @GetMapping("/findby/{id}")
//    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
//        Address address = addressService.getAddressById(id);
//        if (address != null) {
//            return ResponseEntity.ok(address);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PostMapping("")
    public ResponseEntity<?> createAddress(@RequestBody Address addressEntity) {
        try {
            Address createdAddress = addressService.createAddress(addressEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@RequestBody Address addressEntity, @PathVariable Long id) {
        Address address = addressService.updateAddress(addressEntity, id);
        if (address != null) {
            return ResponseEntity.ok(address);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
