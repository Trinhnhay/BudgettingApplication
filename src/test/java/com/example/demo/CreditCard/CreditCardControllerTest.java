package com.example.demo.CreditCard;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CreditCardController.class})
@ExtendWith(SpringExtension.class)
class CreditCardControllerTest {
    @Autowired
    private CreditCardController creditCardController;

    @MockBean
    private CreditCardService creditCardService;

    @Test
    void addCreditCardTest() throws Exception {
        doNothing().when(creditCardService).addCreditCard(Mockito.<CreditCard>any(), Mockito.<String>any());
        CreditCard creditCard = new CreditCard("4322","issuer",0.3,5000.0,500.0,4500.0,50.0, 500.0,"janedoe");
        String content = (new ObjectMapper()).writeValueAsString(creditCard);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/credit_card/{username}/add_new_card", "janedoe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(creditCardController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getCardsByUsernameTest () throws Exception{
        when(creditCardService.getCardsByUsername(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/credit_card/{username}", "janedoe");
        MockMvcBuilders.standaloneSetup(creditCardController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));


    }

    @Test
    void getCurrentBalanceTest() throws Exception{
        when(creditCardService.getCurrentBalance(Mockito.<String>any())).thenReturn(Mockito.<Double>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/credit_card/current-balance-of-{cardNumber}", "4322");
        MockMvcBuilders.standaloneSetup(creditCardController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
}