package com.example.demo.service.sale;

import com.example.demo.entity.Sale;
import com.example.demo.repository.sale.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DiscountStatusUpdater {

    @Autowired
    private SaleRepository saleRepository;

    @Scheduled(fixedRate = 60000)
    public void updateDiscountStatus() {
        Date now = new Date();
        List<Sale> sales = saleRepository.findAllByStatusNot(3);

        for (Sale sale : sales) {
            if (sale.getEndDate() != null && now.after(sale.getStartDate()) && now.before(sale.getEndDate())) {
                sale.setStatus(1);
            } else if (sale.getEndDate() != null && sale.getEndDate().before(now)) {
                sale.setStatus(3);
            }
            saleRepository.save(sale);
        }
    }
}