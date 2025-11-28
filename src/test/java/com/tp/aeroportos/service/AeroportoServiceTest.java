package com.tp.aeroportos.service;

import com.tp.aeroportos.model.Aeroporto;
import com.tp.aeroportos.repository.AeroportoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AeroportoServiceTest {

    @Mock
    private AeroportoRepository aeroportoRepository;

    @InjectMocks
    private AeroportoService aeroportoService;

    @Test
    @DisplayName("Deve salvar aeroporto com sucesso quando IATA não existe")
    void deveSalvarAeroportoComSucesso() {
        Aeroporto aeroporto = new Aeroporto(null, "Guarulhos", "GRU", "São Paulo", "BR", -23.0, -46.0, 750.0);
        
        //simula que o banco n encontrou ninguem com esse IATA
        when(aeroportoRepository.findByIata("GRU")).thenReturn(Optional.empty());
        //simula que o save retornou o objeto
        when(aeroportoRepository.save(any(Aeroporto.class))).thenReturn(aeroporto);

        Aeroporto salvo = aeroportoService.salvar(aeroporto);

        assertNotNull(salvo);
        assertEquals("GRU", salvo.getIata());
        verify(aeroportoRepository, times(1)).save(aeroporto);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar IATA duplicado")
    void deveLancarErroIataDuplicado() {
        //aeroporto com IATA "GRU"
        Aeroporto aeroporto = new Aeroporto(null, "Teste", "GRU", "SP", "BR", 0.0, 0.0, 0.0);
        
        //simula que o banco ja achou um aeroporto com esse IATA
        when(aeroportoRepository.findByIata("GRU")).thenReturn(Optional.of(aeroporto));

        assertThrows(IllegalArgumentException.class, () -> aeroportoService.salvar(aeroporto));
        
        verify(aeroportoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro quando IATA não tiver 3 letras")
    void deveLancarErroIataTamanhoInvalido() {
        //IATA com 4 letras
        Aeroporto aeroporto = new Aeroporto(null, "Teste", "ABCD", "SP", "BR", 0.0, 0.0, 0.0);
        
        assertThrows(IllegalArgumentException.class, () -> aeroportoService.salvar(aeroporto));
        
        verify(aeroportoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro quando altitude for negativa")
    void deveLancarErroAltitudeNegativa() {
        //altitude negativa (-10.0)
        Aeroporto aeroporto = new Aeroporto(null, "Teste", "TST", "SP", "BR", 0.0, 0.0, -10.0);
        
        assertThrows(IllegalArgumentException.class, () -> aeroportoService.salvar(aeroporto));
        
        verify(aeroportoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar aeroporto inexistente")
    void deveLancarErroAtualizarNaoEncontrado() {
        //tenta atualizar o IATA que não existe no banco
        Aeroporto atualizacao = new Aeroporto(null, "Novo Nome", "XXX", "Cidade", "BR", 0.0, 0.0, 0.0);
        
        when(aeroportoRepository.findByIata("XXX")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> aeroportoService.atualizar("XXX", atualizacao));
    }
}