package com.example.demo.repository.onlineShop;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Color;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLBillDetailRepository2 extends JpaRepository<BillDetail, Long> {

    List<BillDetail> findByProductDetail(ProductDetail productDetail);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.bill.id = :billId")
    List<BillDetail> findByBillId(@Param("billId") Long billId);

    List<BillDetail> findAllByBillIdOrderByIdDesc(Long billId);

//    lấy số lượng sản phẩm đã bán đc
@Query("SELECT SUM(bd.quantity) " +
        "FROM BillDetail bd " +
        "JOIN bd.bill b " +
        "JOIN bd.productDetail pd " +
        "WHERE pd.product.id = :idProduct " +
        "AND (" +
        "    EXISTS (" +
        "        SELECT 1 FROM BillHistory bh " +
        "        WHERE bh.bill.id = b.id AND bh.status = 32" +
        "    ) OR (" +
        "        EXISTS (" +
        "            SELECT 1 FROM BillHistory bh " +
        "            WHERE bh.bill.id = b.id AND bh.status = 21" +
        "        ) AND NOT EXISTS (" +
        "            SELECT 1 FROM BillHistory bh2 " +
        "            WHERE bh2.bill.id = b.id AND bh2.status = 32" +
        "        )" +
        "    )" +
        ")")
Integer getTotalQuantitySold(@Param("idProduct") Long idProduct);
}
