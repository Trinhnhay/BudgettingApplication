package com.example.demo.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(path="customer")
public class CustomerController {
    private final CustomerService CustomerService;

    @Autowired
    public CustomerController(CustomerService CustomerService){
        this.CustomerService=CustomerService;
    }

    @GetMapping
    public List<Customer> getCustomer(){
        return CustomerService.getCustomer();
    }


    //Login to an Account
    @PostMapping(path = "login")
    public void loginToAccount (@RequestBody Customer Customer){
        CustomerService.accountLogin(Customer);
    }

    //Register an account
    @PostMapping(path = "registration")
    public void registerNewAccount(@RequestBody Customer Customer ){CustomerService.addNewAccount(Customer);
    }

    //Delete an account
    @DeleteMapping(path="delete/{username}")
    public void deleteCustomer (@PathVariable ("username")String username){CustomerService.deleteCustomer(username);
    }

    //Edit account information
    @PutMapping(path="update/{username}")
    public void uppdateCustomer(
            @PathVariable("username") String username,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address
    ){
        CustomerService.updateCustomer(username, password, firstName, lastName, phoneNumber, email, address);
    }
}
