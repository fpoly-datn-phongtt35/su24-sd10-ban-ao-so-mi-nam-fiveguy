package com.example.demo.repository.nguyen.bill;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface NBillDetailRepository extends JpaRepository<BillDetail, Long> {

    List<BillDetail> findByBillId(Long billId);

    List<BillDetail> findAllByBillIdOrderByIdDesc(Long billId);

    Optional<BillDetail> findByBillAndProductDetail(Bill bill, ProductDetail productDetail);

    @Query("SELECT SUM(CASE WHEN bd.promotionalPrice > 0 THEN bd.promotionalPrice ELSE bd.price END * bd.quantity) FROM BillDetail bd WHERE bd.bill.id = :billId")
    BigDecimal sumTotalAmountByBillId(@Param("billId") Long billId);
}
