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
public interface SaleRepository2 extends JpaRepository<Sale, Long> , JpaSpecificationExecutor<Sale> {

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.status IN (1, 4)")
    Long countSalesWithStatusOneOrFour();


    @Query("SELECT COUNT(s) FROM Sale s WHERE s.status = 3")
    Long countSalesWithStatusThree();


    @Query("SELECT COUNT(s) FROM Sale s WHERE s.status = 2")
    Long countSalesWithStatusTwo();


    List<Sale> findAllByStatusNot(int status);

    // Hàm thực hiện truy vấn đầu tiên: Tổng quan về doanh số của mỗi sale
    @Query(value = "SELECT " +
            "    s.id AS saleId, " +
            "    SUM(bd.quantity - ISNULL(ro.returnQuantity, 0)) AS totalProductsSold, " +
            "    SUM(bd.promotionalPrice * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalRevenue " +
            "FROM " +
            "    Sales s " +
            "    JOIN ProductSales ps ON s.id = ps.IdSale " +
            "    JOIN ProductDetails pd ON ps.IdProduct = pd.IdProduct " +
            "    JOIN BillDetails bd ON pd.id = bd.IdProductDetail " +
            "    JOIN Bills b ON bd.IdBill = b.id " +
            "    LEFT JOIN (" +
            "        SELECT " +
            "            r.IdBillDetail, " +
            "            SUM(r.Quantity) AS returnQuantity " +
            "        FROM " +
            "            ReturnOrders r " +
            "            JOIN PaymentStatus p ON r.IdBill = p.BillId " +
            "        WHERE " +
            "            p.PaymentType = 4 " +
            "        GROUP BY " +
            "            r.IdBillDetail " +
            "    ) ro ON bd.id = ro.IdBillDetail " +
            "WHERE " +
            "    s.id = :saleId " +
            "    AND EXISTS (" +
            "        SELECT 1 FROM BillHistories bh " +
            "        WHERE bh.BillId = b.id AND bh.Status = 21 " +
            "    ) " +
            "GROUP BY " +
            "    s.id", nativeQuery = true)
    SaleSummaryResponse findSaleSummaryById(@Param("saleId") Long saleId);



    @Query(value = "SELECT " +
            "    s.id AS saleId, " +
            "    c.id AS customerId, " +
            "    c.fullName AS customerName, " +
            "    a.phoneNumber AS customerPhone, " +
            "    a.email AS customerEmail, " +
            "    SUM(bd.quantity - ISNULL(ro.returnQuantity, 0)) AS numberOfPurchases, " +
            "    SUM(bd.price * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalAmountBeforeDiscount, " +
            "    SUM(bd.promotionalPrice * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalAmountAfterDiscount, " +
            "    SUM((bd.price - bd.promotionalPrice) * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalDiscountAmount, " +
            "    SUM(bd.promotionalPrice * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalRevenue " +
            "FROM " +
            "    Sales s " +
            "    JOIN ProductSales ps ON s.id = ps.IdSale " +
            "    JOIN ProductDetails pd ON ps.IdProduct = pd.IdProduct " +
            "    JOIN BillDetails bd ON pd.id = bd.IdProductDetail " +
            "    JOIN Bills b ON bd.IdBill = b.id " +
            "    JOIN Customers c ON b.IdCustomer = c.id " +
            "    JOIN Accounts a ON c.IdAccount = a.id " +
            "    LEFT JOIN (" +
            "        SELECT " +
            "            r.IdBillDetail, " +
            "            SUM(r.Quantity) AS returnQuantity " +
            "        FROM " +
            "            ReturnOrders r " +
            "            JOIN PaymentStatus p ON r.IdBill = p.BillId " +
            "        WHERE " +
            "            p.PaymentType = 4 " +
            "        GROUP BY " +
            "            r.IdBillDetail " +
            "    ) ro ON bd.id = ro.IdBillDetail " +
            "WHERE " +
            "    s.id = :saleId " +
            "    AND EXISTS (" +
            "        SELECT 1 " +
            "        FROM BillHistories bh " +
            "        WHERE bh.BillId = b.id " +
            "        AND bh.Status = 21 " +
            "    ) " +
            "    AND bd.promotionalPrice > 0 " +
            "GROUP BY " +
            "    s.id, c.id, c.fullName, a.phoneNumber, a.email",
            nativeQuery = true)
    List<SaleDetailResponse> findSaleDetailsById(@Param("saleId") Long saleId);



    // Hàm thực hiện lấy thông tin sản phẩm của khách hàng
    @Query(value = "SELECT " +
            "    p.name AS productName, " +
            "    SUM(bd.quantity - ISNULL(ro.returnQuantity, 0)) AS totalQuantityBought, " +
            "    bd.price AS originalPrice, " +
            "    bd.promotionalPrice AS promotionalPrice, " +
//            "    SUM(bd.price * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalAmountBeforeDiscount, " +
//            "    SUM(bd.promotionalPrice * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalAmountAfterDiscount, " +
//            "    SUM((bd.price - bd.promotionalPrice) * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalDiscountAmount, " +
            "    SUM((bd.price - bd.promotionalPrice) * (bd.quantity - ISNULL(ro.returnQuantity, 0))) AS totalDiscountAmount " +
            "FROM " +
            "    Sales s " +
            "    JOIN ProductSales ps ON s.id = ps.IdSale " +
            "    JOIN ProductDetails pd ON ps.IdProduct = pd.IdProduct " +
            "    JOIN BillDetails bd ON pd.id = bd.IdProductDetail " +
            "    JOIN Products p ON pd.IdProduct = p.id " +
            "    JOIN Bills b ON bd.IdBill = b.id " +
            "    JOIN Customers c ON b.IdCustomer = c.id " +
            "    JOIN Accounts a ON c.IdAccount = a.id " +
            "    LEFT JOIN (" +
            "        SELECT " +
            "            r.IdBillDetail, " +
            "            SUM(r.Quantity) AS returnQuantity " +
            "        FROM " +
            "            ReturnOrders r " +
            "            JOIN PaymentStatus p ON r.IdBill = p.BillId " +
            "        WHERE " +
            "            p.PaymentType = 4 " +
            "        GROUP BY " +
            "            r.IdBillDetail " +
            "    ) ro ON bd.id = ro.IdBillDetail " +
            "WHERE " +
            "    s.id = :saleId " +
            "    AND c.id = :customerId " +
            "    AND EXISTS (" +
            "        SELECT 1 " +
            "        FROM BillHistories bh " +
            "        WHERE bh.BillId = b.id " +
            "        AND bh.Status = 21 " +
            "    ) " +
            "    AND bd.promotionalPrice > 0 " +
            "GROUP BY " +
            "    p.name, bd.price, bd.promotionalPrice " +
            "ORDER BY " +
            "    p.name",
            nativeQuery = true)
    List<ProductDetailResponse> findProductDetailsBySaleAndCustomer(
            @Param("saleId") Long saleId,
            @Param("customerId") Long customerId
    );




}