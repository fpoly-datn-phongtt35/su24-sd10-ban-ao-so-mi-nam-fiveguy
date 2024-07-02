package com.example.demo.repository.nguyen.bill;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NBillDetailRepository extends JpaRepository<BillDetail, Long> {

    List<BillDetail> findByBillId(Long billId);

    List<BillDetail> findAllByBillIdOrderByIdDesc(Long billId);

    Optional<BillDetail> findByBillAndProductDetail(Bill bill, ProductDetail productDetail);
}
