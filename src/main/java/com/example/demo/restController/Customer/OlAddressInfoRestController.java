package com.example.demo.restController.Customer;

import com.example.demo.entity.Address;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Employee;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.Customer.OlAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ol/authenticated")
public class OlAddressInfoRestController {

    @Autowired
    private OlAddressService olAddressService;

    @Autowired
    private SCCustomerService customerService;

    @Autowired
    private SCEmployeeService scEmployeeService;

    @GetMapping("/address")
    public List<Address> oladdress(@RequestHeader("Authorization") String token) {

        Optional<Customer> customer = customerService.getCustomerByToken(token);
        if(customer.isPresent()){
            return olAddressService.getAddressListByIdCustomer(customer.get().getId());
        }

        return null;
    }

//    @GetMapping("/address/{id}")
//    public AddressEntity getAddress(@PathVariable Long id) {
//        return olAddressService.findById(id).get();
//    }

    @GetMapping("/addressDefault")
    public Address getAddressDefault(@RequestHeader("Authorization") String token) {
        Optional<Customer> customer = customerService.getCustomerByToken(token);
        if(customer.isPresent()){
            return olAddressService.findByDefaultAddressTrue(customer.get().getId());
        }

        return null;

    }

    // Trong controller của bạn
    @DeleteMapping("/deleteAddress/{id}")
    public void deleteAddressById(@PathVariable Long id) {
        // Gọi service để xóa địa chỉ với ID tương ứng
        olAddressService.deleteAddress(id);
    }

    @PostMapping("/updateAddress")
    public boolean updateUser(@RequestBody Address userInfoRequest,@RequestHeader("Authorization") String token) {

        Optional<Customer> customer = customerService.getCustomerByToken(token);
        if(customer.isPresent()){
            userInfoRequest.setCustomer(customer.get());
            return olAddressService.update(userInfoRequest);

        }


        return false;
    }


    @PostMapping("/addAddress")
    public boolean addAddress(@RequestBody Address addressRequest,@RequestHeader("Authorization") String token) {

        Optional<Customer> customer = customerService.getCustomerByToken(token);
        if(customer.isPresent()){
            addressRequest.setCustomer(customer.get());
            return olAddressService.addAddress(addressRequest);
        }
        return false;
    }


}
