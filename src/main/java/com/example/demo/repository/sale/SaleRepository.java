package com.example.demo.repository.sale;

import com.example.demo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> , JpaSpecificationExecutor<Sale> {

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.startDate <= :currentDate AND s.endDate >= :currentDate")
    Long countCurrentSales(Date currentDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.startDate > :currentDate")
    Long countUpcomingSales(Date currentDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.endDate < :currentDate")
    Long countExpiredSales(Date currentDate);

    @Query("SELECT s FROM Sale s WHERE LOWER(s.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Sale> searchByCodeNameValue(@Param("searchTerm") String searchTerm);
}