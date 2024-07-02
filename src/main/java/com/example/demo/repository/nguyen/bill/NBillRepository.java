package com.example.demo.repository.nguyen.bill;

import com.example.demo.entity.Bill;
import com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NBillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {

    @Query("SELECT b FROM Bill b WHERE b.voucher.id = :voucherId")
    List<Bill> findByVoucherId(@Param("voucherId") Long voucherId);

    @Query("SELECT b FROM Bill b " +
            "JOIN b.customer c " +
            "WHERE (:code IS NULL OR b.code LIKE %:code%) " +
            "AND (:customerName IS NULL OR c.fullName LIKE %:customerName%) " +
            "AND (:phoneNumber IS NULL OR b.phoneNumber LIKE %:phoneNumber%) " +
            "AND (:typeBill IS NULL OR b.typeBill = :typeBill) " +
            "AND (:createdAt IS NULL OR b.createdAt = :createdAt) " +
            "AND (:status IS NULL OR b.status = :status)")
    List<Bill> findByFilter(
            @Param("code") String code,
            @Param("customerName") String customerName,
            @Param("phoneNumber") String phoneNumber,
            @Param("typeBill") Integer typeBill,
            @Param("createdAt") Date createdAt,
            @Param("status") Integer status);

    //thong ke theo voucherid
    @Query("SELECT new com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO(c.fullName, a.phoneNumber, a.email, COUNT(b), " +
            "SUM(b.totalAmount), SUM(b.totalAmountAfterDiscount)) " +
            "FROM Bill b " +
            "JOIN b.customer c " +
            "JOIN c.account a " +
            "WHERE b.voucher.id = :voucherId " +
            "GROUP BY c.id, c.fullName, a.phoneNumber, a.email")
    Page<CustomerVoucherStatsDTO> findCustomerVoucherStatsByVoucherId(
            @Param("voucherId") Long voucherId, Pageable pageable);
}
