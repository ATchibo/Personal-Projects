package com.example.assignment1_1.controller;

import com.example.assignment1_1.dtos.DealershipByAvgCarPrice;
import com.example.assignment1_1.service.DealershipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DealershipController.class)
public class DealershipControllerTests {

    @MockBean
    private DealershipService dealershipService;

    @InjectMocks
    private DealershipController dealershipController;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getProperDealershipSortingByCarsValue() throws Exception {
        DealershipByAvgCarPrice dealershipByAvgCarPrice1 = new DealershipByAvgCarPrice(1l, "Auto Rulate Suceava", 1500.0);
        DealershipByAvgCarPrice dealershipByAvgCarPrice2 = new DealershipByAvgCarPrice(2l, "Uzate Pitesti", 3500.0);
        DealershipByAvgCarPrice dealershipByAvgCarPrice3 = new DealershipByAvgCarPrice(3l, "Mihai Cars", 5500.0);

        List<DealershipByAvgCarPrice> dealershipByAvgCarPriceList = new ArrayList<>();
        dealershipByAvgCarPriceList.add(dealershipByAvgCarPrice3);
        dealershipByAvgCarPriceList.add(dealershipByAvgCarPrice2);
        dealershipByAvgCarPriceList.add(dealershipByAvgCarPrice1);

        String jsonString = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(dealershipByAvgCarPriceList);

//        given(dealershipService.findDealershipByAvgCarPrice(0, 25)).willReturn(dealershipByAvgCarPriceList);
//
//        mvc.perform(get("/api/dealerships/sort-by-avg-car-price"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith("application/json"))
//                .andExpect(content().json(jsonString));
//
//        mvc.perform(post("/api/dealerships/sort-by-avg-car-price"))
//                .andExpect(status().is4xxClientError());
    }
}
