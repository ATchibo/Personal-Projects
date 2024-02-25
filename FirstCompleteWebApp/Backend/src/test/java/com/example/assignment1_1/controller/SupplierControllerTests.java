package com.example.assignment1_1.controller;

import com.example.assignment1_1.components.SupplierModelAssembler;
import com.example.assignment1_1.dtos.SupplierByNrShippings;
import com.example.assignment1_1.repo.DealershipRepo;
import com.example.assignment1_1.repo.ShippingContractRepo;
import com.example.assignment1_1.repo.SupplierRepo;
import com.example.assignment1_1.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SupplierController.class)
public class SupplierControllerTests {

    @MockBean
    private SupplierService supplierService;
    @InjectMocks
    private SupplierController supplierController;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getSuppliersByNrOfShippings() throws Exception {
        SupplierByNrShippings supplierByNrShippings1 = new SupplierByNrShippings(1l, "Dacia", 1L);
        SupplierByNrShippings supplierByNrShippings2 = new SupplierByNrShippings(2l, "Renault", 2L);
        SupplierByNrShippings supplierByNrShippings3 = new SupplierByNrShippings(3l, "Volkswagen", 3L);

        List<SupplierByNrShippings> supplierByNrShippingsList = List.of(supplierByNrShippings3, supplierByNrShippings2, supplierByNrShippings1);

        String jsonString = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(supplierByNrShippingsList);

        given(supplierService.findAllSuppliersByNrShippings(0, 3)).willReturn(supplierByNrShippingsList);

//        mvc.perform(get("/api/suppliers/by-nr-contracts"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith("application/json"))
//                .andExpect(content().json(jsonString));
//
//        mvc.perform(post("/api/suppliers/by-nr-contracts"))
//                .andExpect(status().is4xxClientError());
    }
}
