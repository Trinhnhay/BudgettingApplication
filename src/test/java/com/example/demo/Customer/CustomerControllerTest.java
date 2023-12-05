package com.example.demo.Customer;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CustomerController.class})
@ExtendWith(SpringExtension.class)
class CustomerControllerTest {
  @Autowired
  private CustomerController customerController;

  @MockBean
  private CustomerService customerService;


  @Test
  void getCustomerTest() throws Exception {
    when(customerService.getCustomer()).thenReturn(new ArrayList<>());
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers");
    MockMvcBuilders.standaloneSetup(customerController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.content().string("[]"));
  }


  @Test
  void deleteCustomerTest() throws Exception {
    doNothing().when(customerService).deleteCustomer(Mockito.<String>any());
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/customers/delete/{username}",
            "janedoe");
    MockMvcBuilders.standaloneSetup(customerController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void updateCustomerTest() throws Exception {
    doNothing().when(customerService)
            .updateCustomer(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                    Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/customers/update/{username}",
            "janedoe");
    MockMvcBuilders.standaloneSetup(customerController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void loginToAccountTest() throws Exception {
    Customer customer = new Customer();
    customer.setAddress("42 Main St");
    customer.setEmail("jane.doe@example.org");
    customer.setFirstName("Jane");
    customer.setLastName("Doe");
    customer.setPassword("iloveyou");
    customer.setPhoneNumber("6625550144");
    customer.setUsername("janedoe");
    when(customerService.accountLogin(Mockito.<Customer>any())).thenReturn(customer);

    Customer customer2 = new Customer();
    customer2.setAddress("42 Main St");
    customer2.setEmail("jane.doe@example.org");
    customer2.setFirstName("Jane");
    customer2.setLastName("Doe");
    customer2.setPassword("iloveyou");
    customer2.setPhoneNumber("6625550144");
    customer2.setUsername("janedoe");
    String content = (new ObjectMapper()).writeValueAsString(customer2);
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/customers/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content);
    MockMvcBuilders.standaloneSetup(customerController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.content()
                    .string(
                            "{\"username\":\"janedoe\",\"password\":\"iloveyou\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"phoneNumber\":\"6625550144"
                                    + "\",\"address\":\"42 Main St\",\"email\":\"jane.doe@example.org\"}"));
  }


  @Test
  void registerNewAccountTest() throws Exception {
    doNothing().when(customerService).addNewAccount(Mockito.<Customer>any());

    Customer customer = new Customer();
    customer.setAddress("42 Main St");
    customer.setEmail("jane.doe@example.org");
    customer.setFirstName("Jane");
    customer.setLastName("Doe");
    customer.setPassword("iloveyou");
    customer.setPhoneNumber("6625550144");
    customer.setUsername("janedoe");
    String content = (new ObjectMapper()).writeValueAsString(customer);
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/customers/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content);
    MockMvcBuilders.standaloneSetup(customerController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
