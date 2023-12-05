package com.example.demo.Customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerTest;

    @BeforeEach
    void setUp() {
        String username = "fakeaccount";
        String password = "password";
        String email = "fake@email.com";
        String firstName = "fake";
        String lastName ="account";
        String phoneNumber = "8163212145";
        String address ="1324 sw fake st";
        Customer customer = new Customer(username, password, firstName, lastName, phoneNumber, address,  email);

        customerTest.save(customer);
    }

    @AfterEach
    void tearDown() {
        customerTest.deleteAll();
    }

    @Test
    void findAnExistUsernameCustomerTest() {
        //given
        String username ="fakeaccount";
        //when
        boolean expected = customerTest.findUsernameCustomer(username).isPresent();
        //then
        assertThat(expected).isTrue();
        assertThat(customerTest.findUsernameCustomer(username)).isPresent();
    }
    @Test
    void findAnNonExistUsernameCustomerTest() {
        //given
        String username ="person";
        //when
        Optional<Customer> expected = customerTest.findUsernameCustomer(username);
        //then
        assertThat(expected).isEmpty();
    }


    @Test
    void findEmailCustomerTest() {
        //given
        String email =  "fake@email.com";
        //when
        Optional<Customer> expected = customerTest.findEmailCustomer(email);
        //test
        assertThat(expected).isPresent();
    }

    @Test
    void loginAnAccountTest() {
        //given
        String username = "fakeaccount";
        String password = "password";
        //when
        Optional<Customer> expected = customerTest.findAnAccount(username, password);
        //test
        assertThat(expected).isPresent();
    }
}