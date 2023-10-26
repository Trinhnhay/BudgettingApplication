package com.example.demo.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path="customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }

    @GetMapping
    public List<Customer> getCustomer(){
        return customerService.getCustomer();
    }


    //Login to an Account
    @PostMapping(path = "login")
    public Customer loginToAccount (@RequestBody Customer Customer){
        return customerService.accountLogin(Customer);
    }

    //Register an account
    @PostMapping(path = "registration")
    public void registerNewAccount(@RequestBody Customer Customer ){customerService.addNewAccount(Customer);
    }

    //Delete an account
    @DeleteMapping(path="delete/{username}")
    public void deleteCustomer (@PathVariable ("username")String username){customerService.deleteCustomer(username);
    }

    //Edit account information
    @PutMapping(path="update/{username}")
    public void updateCustomer(
            @PathVariable("username") String username,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address
    ){
        customerService.updateCustomer(username, password, firstName, lastName, phoneNumber, email, address);
    }
}
