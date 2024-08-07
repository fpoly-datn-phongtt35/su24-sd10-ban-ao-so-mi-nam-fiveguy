package com.example.demo.repository.thuong;

import com.example.demo.entity.Bill;
import com.example.demo.model.response.thuong.BillResponseTH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepositoryTH extends JpaRepository<Bill, Long> {
    @Query(value = "SELECT NEW com.example.demo.model.response.thuong.BillResponseTH(b.id, b.code, b.reciverName, b.deliveryDate, b.shippingFee, b.addressId, b.address, b.phoneNumber, b.totalAmount, b.totalAmountAfterDiscount, b.paidAmount, b.paidShippingFee, b.createdAt, b.customer, b.employee, b.paymentMethod, b.voucher, b.typeBill, b.note, b.status) FROM Bill b LEFT JOIN b.customer LEFT JOIN b.employee LEFT JOIN b.paymentMethod LEFT JOIN b.voucher WHERE b.status = :status AND b.typeBill = :typeBill")
    List<BillResponseTH> findAllByStatusAndTypeBill(@Param("status") Integer status, @Param("typeBill") Integer typeBill);
}
