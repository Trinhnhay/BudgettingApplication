package com.example.demo.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {

    @Query("SELECT s From Customer s WHERE s.username = ?1")
    Optional<Customer> findUsernameCustomer (String username);

    @Query("SELECT s From Customer s WHERE s.email = ?1")
    Optional <Customer>  findEmailCustomer (String email);

    @Query("SELECT s From Customer s WHERE s.username = ?1 and s.password=?2")
    Optional <Customer> findAnAccount(String username, String password);
}
