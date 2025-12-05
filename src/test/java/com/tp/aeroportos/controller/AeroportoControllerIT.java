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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @DisplayName("Deve criar um aeroporto com sucesso")
    void deveCriarAeroporto() throws Exception {
        Aeroporto novo = new Aeroporto(null, "Aeroporto Teste", "TST", "Cidade Teste", "BR", -10.0, -20.0, 800.0);

        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.iata").value("TST"));
    }

    @Test
    @DisplayName("Deve buscar um aeroporto pelo IATA")
    void deveBuscarAeroporto() throws Exception {
        aeroportoRepository.save(new Aeroporto(null, "Aeroporto Busca", "BSC", "Cidade", "BR", 0.0, 0.0, 0.0));

        mockMvc.perform(get("/api/v1/aeroportos/{iata}", "BSC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Aeroporto Busca"));
    }

    @Test
    @DisplayName("Deve atualizar os dados de um aeroporto")
    void deveAtualizarAeroporto() throws Exception {
        aeroportoRepository.save(new Aeroporto(null, "TESTE", "UPD", "Cidade", "BR", 0.0, 0.0, 0.0));
        Aeroporto atualizado = new Aeroporto(null, "TESTENOVO", "UPD", "CIDADETESTE", "BR", 1.0, 1.0, 10.0);

        mockMvc.perform(put("/api/v1/aeroportos/{iata}", "UPD")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("TESTENOVO"));
    }

    @Test
    @DisplayName("Deve excluir um aeroporto")
    void deveDeletarAeroporto() throws Exception {
        aeroportoRepository.save(new Aeroporto(null, "Para Deletar", "DEL", "Cidade", "BR", 0.0, 0.0, 0.0));

        mockMvc.perform(delete("/api/v1/aeroportos/{iata}", "DEL"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar aeroporto excluído")
    void deveRetornar404AposDelete() throws Exception {
        aeroportoRepository.save(new Aeroporto(null, "IpatingaAP", "GHO", "Ipatinga", "BR", 0.0, 0.0, 0.0));
        mockMvc.perform(delete("/api/v1/aeroportos/{iata}", "GHO")).andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/aeroportos/{iata}", "GHO"))
                .andExpect(status().isNotFound());
    }
}