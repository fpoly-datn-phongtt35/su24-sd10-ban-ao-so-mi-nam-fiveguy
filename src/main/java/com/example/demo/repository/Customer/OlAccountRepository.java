package com.example.demo.repository.Customer;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OlAccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccount(String username);
}
