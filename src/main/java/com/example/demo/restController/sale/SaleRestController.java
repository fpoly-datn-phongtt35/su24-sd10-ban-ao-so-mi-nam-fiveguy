package com.example.demo.restController.sale;

import com.example.demo.entity.Sale;
import com.example.demo.model.response.sale.SaleSummaryResponse;
import com.example.demo.service.sale.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

//    @GetMapping("/search")
//    public List<Sale> searchSales(@RequestParam(value = "searchTerm") String searchTerm) {
//        return saleService.searchByCodeNameValue(searchTerm);
//    }


    @GetMapping("/fill")
    public Page<Sale> getSalesByConditions(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Integer discountType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Date parsedStartDate = null;
        Date parsedEndDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            if (startDate != null) {
                parsedStartDate = dateFormat.parse(startDate);
            }
            if (endDate != null) {
                parsedEndDate = dateFormat.parse(endDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Sale> result = saleService.findSalesByConditions(parsedStartDate, parsedEndDate, status, searchTerm, discountType, pageable);
        return result;
    }



    @GetMapping("/summary/{id}")
    public SaleSummaryResponse getSaleSummary(@PathVariable("id") Long saleId) {
        return saleService.getSaleSummaryById(saleId);
    }


}