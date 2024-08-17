package com.example.demo.restController.thuong;

import com.example.demo.entity.Address;
import com.example.demo.service.thuong.AddressServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/address-th")
public class AddressControllerTH {
    @Autowired
    private AddressServiceTH addressService;

    @PostMapping
    public ResponseEntity<?> saveAllAddress(@RequestBody List<Address> addressList) {
        return new ResponseEntity<>(addressService.saveAll(addressList), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody Address address) {
        return new ResponseEntity<>(addressService.update(address), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id) {
        return new ResponseEntity<>(addressService.deleteAddress(id), HttpStatus.OK);
    }
}
