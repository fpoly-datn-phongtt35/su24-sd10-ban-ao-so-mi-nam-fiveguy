package com.example.demo.restController.onlineShop;

import com.example.demo.entity.Address;
import com.example.demo.entity.Customer;
import com.example.demo.repository.onlineShop.OLAddressRepository2;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.onlineShop.OLAddressService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLAddressController2 {

    @Autowired
    private OLAddressService2 olAddressService;

    @Autowired
    private SCCustomerService SCCustomerService;


    @GetMapping("/address")
    public ResponseEntity<List<Address>> oladdress(@RequestHeader("Authorization") String token) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);

        if (customer.isPresent()) {
            List<Address> addresses = olAddressService.getAddressListByUsername(customer.get());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        }
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK); // Trả về danh sách rỗng nếu không có địa chỉ
    }

    @GetMapping("/addressDefault")
    public ResponseEntity<Address> getAddressDefault(@RequestHeader("Authorization") String token) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);

        if (customer.isPresent()) {
            Address defaultAddress = olAddressService.findByDefaultAddressTrue(customer.get());
            return new ResponseEntity<>(defaultAddress, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK); // Trả về null nếu không có địa chỉ mặc định
    }


}
