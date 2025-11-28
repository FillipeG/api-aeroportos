package com.tp.aeroportos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.aeroportos.model.Aeroporto;
import com.tp.aeroportos.repository.AeroportoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
class AeroportoControllerIT {

    @Autowired
    private MockMvc mockMvc; 

    @Autowired
    private AeroportoRepository aeroportoRepository; 

    @Autowired
    private ObjectMapper objectMapper; 

    @BeforeEach
    void setUp() {
        aeroportoRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/v1/aeroportos - Deve criar aeroporto e retornar 201")
    void deveCriarAeroporto() throws Exception {
        //criar novo aeroporto
        Aeroporto novo = new Aeroporto(null, "Aeroporto Teste", "TST", "Cidade Teste", "BR", -10.0, -20.0, 800.0);

        //POST que envia o JSON
        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.iata").value("TST")) 
                .andExpect(jsonPath("$.id").exists()); 
    }
}