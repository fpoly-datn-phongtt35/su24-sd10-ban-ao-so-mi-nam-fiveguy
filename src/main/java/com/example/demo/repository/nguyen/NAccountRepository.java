package com.example.demo.repository.nguyen;

import com.example.demo.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NAccountRepository extends JpaRepository<Account, Long> {

    Page<Account> findByPhoneNumberContainingOrEmailContaining(String phoneNumber, String email, Pageable pageable);

}
