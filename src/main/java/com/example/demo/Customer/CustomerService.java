package com.example.demo.Customer;

import com.example.demo.Exception.RequestException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository CustomerRepository;

    @Autowired
    public CustomerService(CustomerRepository CustomerRepository) {
        this.CustomerRepository = CustomerRepository;
    }

    public List<Customer> getCustomer() {
        return CustomerRepository.findAll();
    }

    public void addNewAccount (Customer Customer){

        Optional <Customer> CustomerOptionalUsername = CustomerRepository
                .findUsernameCustomer(Customer.getUsername());

        Optional <Customer> CustomerOptionalEmail = CustomerRepository
                .findEmailCustomer(Customer.getEmail());

        if (CustomerOptionalUsername.isPresent()){
            throw new RequestException("This username is used");
        }
        if (CustomerOptionalEmail.isPresent()){
            throw new RequestException("This email address is used");
        }
        CustomerRepository.save(Customer);
    }

    public void deleteCustomer(String username){

        if (!CustomerRepository.existsById(username)){
            throw new RequestException("The username does not exist");
        }
        CustomerRepository.deleteById(username);
    }

    @Transactional
    public void updateCustomer(String username, String password, String firstName, String lastName , String phoneNumber, String email, String address){
        Customer customer = CustomerRepository.findById(username)
                .orElseThrow(()-> new RequestException(
                        "This account does not exist"
                ));
        if(password !=null && !password.isEmpty() && !Objects.equals(customer.getPassword(),password)){
            customer.setPassword(password);
        }
        if(firstName !=null && !firstName.isEmpty() && !Objects.equals(customer.getFirstName(),firstName)){
            customer.setFirstName(firstName);
        }

        if(lastName !=null && !lastName.isEmpty() && !Objects.equals(customer.getLastName(),lastName)){
            customer.setLastName(lastName);
        }
        if(phoneNumber !=null && !phoneNumber.isEmpty() && !Objects.equals(customer.getPhoneNumber(),phoneNumber)){
            customer.setPhoneNumber(phoneNumber);
        }
        if(email !=null && !email.isEmpty() && !Objects.equals(customer.getEmail(),email)){
            customer.setEmail(email);
        }
        if(address !=null && !address.isEmpty() && !Objects.equals(customer.getAddress(),address)){
            customer.setAddress(address);
        }
    }

    public Customer accountLogin(Customer customer) {
        Optional<Customer> customerOptionalUsername = CustomerRepository
                .findAnAccount(customer.getUsername(), customer.getPassword());
        if (customerOptionalUsername.isEmpty()){
            throw new RequestException("Invalid username/password");
        }
        return customerOptionalUsername.get();
    }
}
