package com.yui.dscatalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yui.dscatalog.dto.ProductDTO;
import com.yui.dscatalog.factory.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProduct;

    private ObjectMapper objectMapper;



    @BeforeEach
    void setUp()throws  Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProduct = 25L;

    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception{
        ResultActions result = mockMvc.perform(
                get("/products?page=0&size=12&sort=name,asc", existingId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(jsonPath("&.totalElements").value(countTotalProduct));
        result.andExpect(jsonPath("&.content").exists());
        result.andExpect(jsonPath("&.content[0].name").value("MacBook Pro"));
        result.andExpect(jsonPath("&.content[1].name").value("Pc Gamer"));
        result.andExpect(jsonPath("&.content[2].name").value("Pc Gamer Alfa"));

    }


    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws  Exception{
        ProductDTO productDTO = Factory.createProductDTO();

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();

        ResultActions result = mockMvc.perform(
                put("/products{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescription));
    }

    @Test
    public void updateShouldReturnNotFoundWhenDoesNotExist() throws  Exception {
        ProductDTO productDTO = Factory.createProductDTO();

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                put("/products{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }
}
