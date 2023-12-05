package com.example.demo.Customer;

import com.example.demo.Exception.RequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerService customerServiceTest;

    @BeforeEach
    void setUp() {
        customerServiceTest = new CustomerService(customerRepository);
    }

    @Test
    void getCustomerTest() {
        //when
        customerServiceTest.getCustomer();
        //then
        verify(customerRepository).findAll();
    }

    @Test
    void addNewAccountTest() {
        //given
        String username = "fakeaccount";
        String password = "password";
        String email = "fake@email.com";
        String firstName = "fake";
        String lastName ="account";
        String phoneNumber = "8163212145";
        String address ="1324 sw fake st";
        Customer customer = new Customer(username, password, firstName, lastName, phoneNumber, address,  email);
        //when
        customerServiceTest.addNewAccount(customer);
        //then
        ArgumentCaptor<Customer> customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer).isEqualTo(customer);
    }

    @Test
    void cannotAddAnAccountWithAnExistEmailTest() {
        //given
        String username = "fakeaccount";
        String password = "password";
        String email = "fake@email.com";
        String firstName = "fake";
        String lastName ="account";
        String phoneNumber = "8163212145";
        String address ="1324 sw fake st";
        Customer customer = new Customer(username, password, firstName, lastName, phoneNumber, address,  email);

        given(customerRepository.findEmailCustomer(anyString())).willReturn(java.util.Optional.of(customer));
        //when
        //then
        assertThatThrownBy(()-> customerServiceTest.addNewAccount(customer))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("This email address is used");

        verify(customerRepository, never()).save(any());
    }
    @Test
    void cannotAddAnAccountWithAnExistUsernameTest() {
        //given
        String username = "fakeaccount";
        String password = "password";
        String email = "fake@email.com";
        String firstName = "fake";
        String lastName ="account";
        String phoneNumber = "8163212145";
        String address ="1324 sw fake st";
        Customer customer = new Customer(username, password, firstName, lastName, phoneNumber, address,  email);

        given(customerRepository.findUsernameCustomer(anyString())).willReturn(java.util.Optional.of(customer));
        //when
        //then
        assertThatThrownBy(()-> customerServiceTest.addNewAccount(customer))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("This username is used");

        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteCustomerTest() {
        doNothing().when(customerRepository).deleteById(any());
        when(customerRepository.existsById(any())).thenReturn(true);
        customerServiceTest.deleteCustomer("janedoe");
        verify(customerRepository).deleteById(any());
        verify(customerRepository).existsById(any());
    }

    @Test
    void deleteANonExistCustomerTest() {
        when(customerRepository.existsById(Mockito.any())).thenReturn(false);
        assertThrows(RequestException.class, () -> customerServiceTest.deleteCustomer("janedoe"));
        verify(customerRepository).existsById(Mockito.any());
    }

    @Test
    void updateCustomerTest() {
        Customer customer = new Customer();
        customer.setAddress("42 Main St");
        customer.setEmail("jane.doe@example.org");
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setPassword("iloveyou");
        customer.setPhoneNumber("6625550144");
        customer.setUsername("janedoe");
        Optional<Customer> ofResult = Optional.of(customer);

        Customer customer2 = new Customer();
        customer2.setAddress("42 Main St");
        customer2.setEmail("jane.doe@example.org");
        customer2.setFirstName("Jane");
        customer2.setLastName("Doe");
        customer2.setPassword("iloveyou");
        customer2.setPhoneNumber("6625550144");
        customer2.setUsername("janedoe");
        Optional<Customer> ofResult2 = Optional.of(customer2);
        when(customerRepository.findEmailCustomer(any())).thenReturn(ofResult2);
        when(customerRepository.findById(Mockito.any())).thenReturn(ofResult);
        assertThrows(RequestException.class, () -> customerServiceTest.updateCustomer("janedoe", "iloveyou", "Jane", "Doe",
                "6625550144", "jane.doe@example.org", "42 Main St"));
        verify(customerRepository).findEmailCustomer(any());
        verify(customerRepository).findById(any());
    }

    @Test
    void updateCustomerTest2() {
        Customer customer = mock(Customer.class);
        when(customer.getAddress()).thenReturn("foo");
        when(customer.getFirstName()).thenReturn("Jane");
        when(customer.getLastName()).thenReturn("Doe");
        when(customer.getPassword()).thenReturn("iloveyou");
        when(customer.getPhoneNumber()).thenReturn("6625550144");
        doNothing().when(customer).setAddress(any());
        doNothing().when(customer).setEmail(any());
        doNothing().when(customer).setFirstName(any());
        doNothing().when(customer).setLastName(any());
        doNothing().when(customer).setPassword(any());
        doNothing().when(customer).setPhoneNumber(any());
        doNothing().when(customer).setUsername(any());
        customer.setAddress("42 Main St");
        customer.setEmail("jane.doe@example.org");
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setPassword("iloveyou");
        customer.setPhoneNumber("6625550144");
        customer.setUsername("janedoe");
        Optional<Customer> ofResult = Optional.of(customer);
        Optional<Customer> emptyResult = Optional.empty();
        when(customerRepository.findEmailCustomer(any())).thenReturn(emptyResult);
        when(customerRepository.findById(any())).thenReturn(ofResult);
        customerServiceTest.updateCustomer("janedoe", "iloveyou", "Jane", "Doe", "6625550144", "jane.doe@example.org",
                "42 Main St");
        verify(customer).getAddress();
        verify(customer).getFirstName();
        verify(customer).getLastName();
        verify(customer).getPassword();
        verify(customer).getPhoneNumber();
        verify(customer, atLeast(1)).setAddress(any());
        verify(customer, atLeast(1)).setEmail(any());
        verify(customer).setFirstName(any());
        verify(customer).setLastName(any());
        verify(customer).setPassword(any());
        verify(customer).setPhoneNumber(any());
        verify(customer).setUsername(any());
        verify(customerRepository).findEmailCustomer(any());
        verify(customerRepository).findById(any());
    }
    @Test
    void accountLoginTest() {
        Customer customer = new Customer();
        customer.setAddress("42 Main St");
        customer.setEmail("jane.doe@example.org");
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setPassword("iloveyou");
        customer.setPhoneNumber("6625550144");
        customer.setUsername("janedoe");
        Optional<Customer> ofResult = Optional.of(customer);
        when(customerRepository.findAnAccount(any(), any())).thenReturn(ofResult);

        Customer customer2 = new Customer();
        customer2.setAddress("42 Main St");
        customer2.setEmail("jane.doe@example.org");
        customer2.setFirstName("Jane");
        customer2.setLastName("Doe");
        customer2.setPassword("iloveyou");
        customer2.setPhoneNumber("6625550144");
        customer2.setUsername("janedoe");
        Customer actualAccountLoginResult = customerServiceTest.accountLogin(customer2);
        verify(customerRepository).findAnAccount(any(), any());
        assertSame(customer, actualAccountLoginResult);
    }
    @Test
    void cannotLoginToAccountTest() {
        Optional<Customer> emptyResult = Optional.empty();
        when(customerRepository.findAnAccount(any(),any())).thenReturn(emptyResult);

        Customer customer = new Customer();
        customer.setAddress("42 Main St");
        customer.setEmail("jane.doe@example.org");
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setPassword("iloveyou");
        customer.setPhoneNumber("6625550144");
        customer.setUsername("janedoe");
        assertThrows(RequestException.class, () -> customerServiceTest.accountLogin(customer));
        verify(customerRepository).findAnAccount(any(), any());
    }

}