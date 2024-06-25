package com.example.demo.repository.sale;

import com.example.demo.entity.Sale;
import com.example.demo.model.response.sale.ProductDetailResponse;
import com.example.demo.model.response.sale.SaleDetailResponse;
import com.example.demo.model.response.sale.SaleSummaryResponse;
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

    List<Sale> findAllByStatusNot(int status);

    // Hàm thực hiện truy vấn đầu tiên: Tổng quan về doanh số của mỗi sale
    @Query("SELECT " +
            "    s.id AS saleId, " +
            "    SUM(bd.quantity) AS totalProductsSold, " +
            "    SUM(bd.promotionalPrice * bd.quantity) AS totalRevenue, " +
            "    SUM((bd.price - bd.promotionalPrice) * bd.quantity) AS totalProfit " +
            "FROM " +
            "    Sale s " +
            "    JOIN ProductSale ps ON s.id = ps.sale.id " +
            "    JOIN ProductDetail pd ON ps.product.id = pd.product.id " +
            "    JOIN BillDetail bd ON pd.id = bd.productDetail.id " +
            "    JOIN Product p ON pd.product.id = p.id " +
            "    JOIN Bill b ON bd.bill.id = b.id " +
            "WHERE " +
            "    s.id = :saleId AND b.status = 1 " +
            "GROUP BY " +
            "    s.id")
    SaleSummaryResponse findSaleSummaryById(@Param("saleId") Long saleId);

    @Query("SELECT " +
            "    s.id AS saleId, " +
            "    c.id AS customerId, " +
            "    c.fullName AS customerName, " +
            "    a.phoneNumber AS customerPhone, " +
            "    a.email AS customerEmail, " +
            "    SUM(bd.quantity) AS numberOfPurchases, " +
            "    SUM(bd.quantity * bd.price) AS totalAmountBeforeDiscount, " +
            "    SUM(bd.quantity * bd.promotionalPrice) AS totalAmountAfterDiscount, " +
            "    SUM(bd.quantity * (bd.price - bd.promotionalPrice)) AS totalDiscountAmount " +
            "FROM " +
            "    Sale s " +
            "    JOIN ProductSale ps ON s.id = ps.sale.id " +
            "    JOIN ProductDetail pd ON ps.product.id = pd.product.id " +
            "    JOIN BillDetail bd ON pd.id = bd.productDetail.id " +
            "    JOIN Product p ON pd.product.id = p.id " +
            "    JOIN Bill b ON bd.bill.id = b.id " +
            "    JOIN Customer c ON b.customer.id = c.id " +
            "    JOIN Account a ON c.account.id = a.id " +
            "WHERE " +
            "    s.id = :saleId AND b.status = 1 AND bd.promotionalPrice > 0 " +
            "GROUP BY " +
            "    s.id, c.id, c.fullName, a.phoneNumber, a.email")
    List<SaleDetailResponse> findSaleDetailsById(@Param("saleId") Long saleId);


    // Hàm thực hiện lấy thông tin sản phẩm của khách hàng
    @Query("SELECT " +
            "    p.name AS productName, " +
            "    SUM(bd.quantity) AS totalQuantityBought, " +
            "    bd.price AS originalPrice, " +
            "    bd.promotionalPrice AS promotionalPrice, " +
            "    SUM(bd.quantity * bd.price) AS totalAmountBeforeDiscount, " +
            "    SUM(bd.quantity * bd.promotionalPrice) AS totalAmountAfterDiscount " +
            "FROM " +
            "    Sale s " +
            "    JOIN ProductSale ps ON s.id = ps.sale.id " +
            "    JOIN ProductDetail pd ON ps.product.id = pd.product.id " +
            "    JOIN BillDetail bd ON pd.id = bd.productDetail.id " +
            "    JOIN Product p ON pd.product.id = p.id " +
            "    JOIN Bill b ON bd.bill.id = b.id " +
            "    JOIN Customer c ON b.customer.id = c.id " +
            "    JOIN Account a ON c.account.id = a.id " +
            "WHERE " +
            "    s.id = :saleId AND b.status = 1 AND c.id = :customerId " +
            "GROUP BY " +
            "    p.name, bd.price, bd.promotionalPrice " +
            "ORDER BY " +
            "    p.name")
    List<ProductDetailResponse> findProductDetailsBySaleAndCustomer(@Param("saleId") Long saleId, @Param("customerId") Long customerId);


}