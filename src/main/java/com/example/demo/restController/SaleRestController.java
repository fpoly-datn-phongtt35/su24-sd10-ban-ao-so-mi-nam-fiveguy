package com.example.demo.restController;

import com.example.demo.entity.Sale;
import com.example.demo.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/sales")
public class SaleRestController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        Sale createdSale = saleService.saveSale(sale);
        return ResponseEntity.ok(createdSale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        Sale updatedSale = saleService.updateSale(id, sale);
        return ResponseEntity.ok(updatedSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Sale sale = saleService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/current/count")
    public ResponseEntity<Long> countCurrentSales() {
        Long count = saleService.countCurrentSales();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/upcoming/count")
    public ResponseEntity<Long> countUpcomingSales() {
        Long count = saleService.countUpcomingSales();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/expired/count")
    public ResponseEntity<Long> countExpiredSales() {
        Long count = saleService.countExpiredSales();
        return ResponseEntity.ok(count);
    }
}