package com.tp.aeroportos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Test
    @DisplayName("GET /api/v1/aeroportos/{iata} - Deve retornar aeroporto e Status 200")
    void deveBuscarAeroporto() throws Exception {
        //salva um aeroporto no banco 
        aeroportoRepository.save(new Aeroporto(null, "Aeroporto Busca", "BSC", "Cidade", "BR", 0.0, 0.0, 0.0));

        //faz o GET e espera JSON 
        mockMvc.perform(get("/api/v1/aeroportos/{iata}", "BSC"))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.nome").value("Aeroporto Busca"));
    }

    @Test
    @DisplayName("PUT /api/v1/aeroportos/{iata} - Deve atualizar e retornar 200")
    void deveAtualizarAeroporto() throws Exception {
        //cria um aeroporto no banco
        aeroportoRepository.save(new Aeroporto(null, "TESTE", "UPD", "Cidade", "BR", 0.0, 0.0, 0.0));

        Aeroporto atualizado = new Aeroporto(null, "TESTENOVO", "UPD", "CIDADETESTE", "BR", 1.0, 1.0, 10.0);

        //faz o PUT enviando o JSON novo
        mockMvc.perform(put("/api/v1/aeroportos/{iata}", "UPD")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.nome").value("TESTENOVO")); 
    }

    @Test
    @DisplayName("DELETE /api/v1/aeroportos/{iata} - Deve excluir (204) e depois não encontrar (404)")
    void deveDeletarAeroporto() throws Exception {
        aeroportoRepository.save(new Aeroporto(null, "Aeroporto Para Deletar", "DEL", "Cidade", "BR", 0.0, 0.0, 0.0));

        //deleta e espera erro
        mockMvc.perform(delete("/api/v1/aeroportos/{iata}", "DEL"))
                .andExpect(status().isNoContent());

        //tenta buscar o mesmo e espera erro
        mockMvc.perform(get("/api/v1/aeroportos/{iata}", "DEL"))
                .andExpect(status().isNotFound());
    }
}