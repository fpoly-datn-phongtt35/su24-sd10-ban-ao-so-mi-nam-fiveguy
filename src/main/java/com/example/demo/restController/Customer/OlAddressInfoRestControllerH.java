package com.example.demo.restController.Customer;

import com.example.demo.entity.Address;
import com.example.demo.security.service.SCUserService;
import com.example.demo.service.Customer.OlAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ol/authenticated")
public class OlAddressInfoRestControllerH {

    @Autowired
    private OlAddressService olAddressService;

    @Autowired
    private SCUserService userService;


    @GetMapping("/address")
    public List<Address> oladdress(@RequestParam("username") String username) {
        return olAddressService.getAddressListByUsername(username);
    }

//    @GetMapping("/address/{id}")
//    public AddressEntity getAddress(@PathVariable Long id) {
//        return olAddressService.findById(id).get();
//    }

    @GetMapping("/addressDefault")
    public Address getAddressDefault(@RequestParam("username") String username) {
        return olAddressService.findByDefaultAddressTrue(username);
    }

    // Trong controller của bạn
    @DeleteMapping("/deleteAddress/{id}")
    public void deleteAddressById(@PathVariable Long id) {
        // Gọi service để xóa địa chỉ với ID tương ứng
        olAddressService.deleteAddress(id);
    }

    @PostMapping("/updateAddress")
    public boolean updateUser(@RequestBody Address userInfoRequest) {
        return olAddressService.update(userInfoRequest);
    }


    @PostMapping("/addAddress")
    public boolean addAddress(@RequestBody Address addressRequest) {
        return olAddressService.addAddress(addressRequest);
    }


}
