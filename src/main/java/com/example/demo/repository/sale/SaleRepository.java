package com.example.demo.repository.sale;

import com.example.demo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.startDate <= :currentDate AND s.endDate >= :currentDate")
    Long countCurrentSales(Date currentDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.startDate > :currentDate")
    Long countUpcomingSales(Date currentDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.endDate < :currentDate")
    Long countExpiredSales(Date currentDate);
}