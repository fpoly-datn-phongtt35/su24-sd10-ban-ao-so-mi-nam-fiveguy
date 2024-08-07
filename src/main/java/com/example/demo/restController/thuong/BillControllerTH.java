package com.example.demo.restController.thuong;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;
import com.example.demo.model.request.thuong.CategoryRequestTH;
import com.example.demo.model.response.thuong.BillResponseTH;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.thuong.BillServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/bill-th")
public class BillControllerTH {
    @Autowired
    private BillServiceTH billService;

    @Autowired
    private SCEmployeeService scEmployeeService;

    @GetMapping
    public ResponseEntity<?> getBills() {
        return ResponseEntity.ok(billService.findAllByStatusAndTypeBill(20, 1));
    }

    @PutMapping("/add-cart/{id}")
    public ResponseEntity<?> addPDCart(@PathVariable Long id, @RequestBody BillResponseTH bill) {
        return new ResponseEntity<>(billService.addProductCart(bill, id), HttpStatus.OK);
    }

    @PutMapping("/remove-cart/{id}")
    public ResponseEntity<?> removePDCart(@PathVariable Long id, @RequestBody BillResponseTH bill) {
        return new ResponseEntity<>(billService.removeProductCart(bill, id), HttpStatus.OK);
    }

    @PutMapping("/update-cart/{id}")
    public ResponseEntity<?> updatePDCart(@PathVariable Long id, @RequestBody BillResponseTH bill, @RequestParam Integer updateQty) {
        return new ResponseEntity<>(billService.updateProductCart(bill, id, updateQty), HttpStatus.OK);
    }

    @PutMapping("/delete-cart/{id}")
    public ResponseEntity<?> deletePDCart(@PathVariable Long id, @RequestBody BillResponseTH bill) {
        return new ResponseEntity<>(billService.deleteProductCart(bill, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete-bill/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Long id) {
        return new ResponseEntity<>(billService.deleteBill(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBill(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<?> createBill(@RequestHeader("Authorization") String token) {
        Optional<Employee> employee = scEmployeeService.getEmployeeByToken(token);

        return new ResponseEntity<>(billService.create(employee.get()), HttpStatus.CREATED);
    }
}
