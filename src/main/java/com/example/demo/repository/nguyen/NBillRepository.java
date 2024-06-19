package com.example.demo.repository.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NBillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT b FROM Bill b WHERE b.voucher.id = :voucherId")
    List<Bill> findByVoucherId(@Param("voucherId") Long voucherId);

//    @Query("SELECT new com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO(c.fullName, b.phoneNumber, a.email, COUNT(b), " +
//            "SUM(b.totalAmount), SUM(b.totalAmountAfterDiscount)) " +
//            "FROM Bill b " +
//            "JOIN b.customer c " +
//            "JOIN c.account a " +
//            "WHERE b.voucher.id = :voucherId " +
//            "GROUP BY c.fullName, b.phoneNumber, a.email")
//    Page<CustomerVoucherStatsDTO> findCustomerVoucherStatsByVoucherId(@Param("voucherId") Long voucherId, Pageable pageable);

    @Query("SELECT new com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO(c.fullName, a.phoneNumber, a.email, COUNT(b), " +
            "SUM(b.totalAmount), SUM(b.totalAmountAfterDiscount)) " +
            "FROM Bill b " +
            "JOIN b.customer c " +
            "JOIN c.account a " +
            "WHERE b.voucher.id = :voucherId " +
            "GROUP BY c.id, c.fullName, a.phoneNumber, a.email")
    Page<CustomerVoucherStatsDTO> findCustomerVoucherStatsByVoucherId(@Param("voucherId") Long voucherId, Pageable pageable);
}
