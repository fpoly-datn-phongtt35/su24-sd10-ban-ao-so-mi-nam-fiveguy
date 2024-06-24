package com.example.demo.repository.Customer;

import com.example.demo.entity.Account;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT A.Id, A.Account, A.Password, A.Email, A.PhoneNumber, A.ConfirmationCode, A.IdRole, A.Status\n" +
            "FROM Accounts A\n" +
            "LEFT JOIN Customers C ON A.Id = C.IdAccount\n" +
            "LEFT JOIN Employees E ON A.Id = E.IdAccount\n" +
            "WHERE C.Id IS NULL AND E.Id IS NULL", nativeQuery = true)
    List<Account> loadAccount();

    @Query("SELECT a FROM Account a WHERE a.status = :a")
    List<Account> getSStatus(Integer a);

    Optional<Account> findByAccount(String account);


    List<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByAccount(String account);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT m FROM Account m WHERE m.email = :status")
    Account getByEmailAccount(String status);

//OL
//    Optional<Account> findByAccountLogin(String username);
//END OL
}
