package com.example.demo.restController.Customer;

import com.example.demo.entity.Customer;
import com.example.demo.repository.Customer.CustomerRepositoryH;
import com.example.demo.untility.tinh.PaginationResponse;
import com.example.demo.service.Customer.CustomerServiceH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customer")

public class CustomerRestControllerH {


    @Autowired
    CustomerServiceH customerService;

//    @Autowired
//    AccountService accountService;

    @Autowired
    CustomerRepositoryH customerRepositoryH;

    @GetMapping("")
    public ResponseEntity<List<Customer>> getAll() {
        List<Customer> customers = customerService.getAll();
        return ResponseEntity.ok(customers);

    }

    @GetMapping("/account/{account}")
    public ResponseEntity<Customer> getByAccount(@PathVariable String account) {
        Customer account1 = customerService.getByAccount(account);
        return ResponseEntity.ok(account1);
    }

    //get employee status =1
    @GetMapping("/status1")
    public ResponseEntity<List<Customer>> getAllStatusDangLam() {
        List<Customer> customers = customerService.getAllStatusDangLam();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        Customer customer = customerService.getById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/search-status/{id}")
    public ResponseEntity<List<Customer>> getAllstatus(@PathVariable Integer id) {
        List<Customer> customers = customerService.getAllStatus(id);
        return ResponseEntity.ok(customers);
    }


    @GetMapping("/get-page")
    public ResponseEntity<Page<Customer>> phantrang(@RequestParam(defaultValue = "0", name = "page") Integer t) {
        Page<Customer> customers = customerService.phanTrang(t, 5);

        return ResponseEntity.ok(customers);
    }

    @PutMapping("/update-status-nhan-vien/{id}")
    public void updateStatus(@PathVariable Long id) {
        customerRepositoryH.updateStatusCustomer(id);
    }


    //Thêm customer

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody Customer customers) {
        try {
            Customer createdCustomer = customerService.create(customers);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // delete customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //update customer
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customers) {
        customerService.update(id, customers);
        if (customers != null) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Customer> updatestatus(@PathVariable Long id, @RequestBody Customer customers) {
        customerService.updateRole(id, customers);
        if (customers != null) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/page")
    public PaginationResponse<Customer> getEmployees(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String avatar,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date birthDate,
            @RequestParam(required = false) Boolean gender,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Long idCustomerType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        Page<Customer> page = customerService.findCustomer(fullName, code, avatar, birthDate, gender, address, account, email, phoneNumber, idCustomerType, status, pageable);
        return new PaginationResponse<>(page);
    }
}
