package com.tp.aeroportos.service;

import com.tp.aeroportos.exception.AeroportoNaoEncontradoException;
import com.tp.aeroportos.model.Aeroporto;
import com.tp.aeroportos.repository.AeroportoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AeroportoServiceTest {

    @Mock
    private AeroportoRepository aeroportoRepository;

    @InjectMocks
    private AeroportoService aeroportoService;

    //IATA que não existe
    @Test
    @DisplayName("Deve lançar AeroportoNaoEncontradoException ao buscar IATA inexistente")
    void deveLancarExcecaoQuandoIataNaoExiste() {
        when(aeroportoRepository.findByIata("ZZZ")).thenReturn(Optional.empty());

        Aeroporto dummy = new Aeroporto();
        assertThrows(AeroportoNaoEncontradoException.class, () -> aeroportoService.atualizar("ZZZ", dummy));
    }

    //Dados invalidos
    @Test
    @DisplayName("Deve rejeitar operação ao tentar salvar com dados inválidos")
    void deveRejeitarDadosInvalidos() {
        Aeroporto iataInvalido = new Aeroporto(null, "Teste", "ABCD", "SP", "BR", 0.0, 0.0, 100.0);
        assertThrows(IllegalArgumentException.class, () -> aeroportoService.salvar(iataInvalido));

        Aeroporto altitudeInvalida = new Aeroporto(null, "Teste", "TST", "SP", "BR", 0.0, 0.0, -10.0);
        assertThrows(IllegalArgumentException.class, () -> aeroportoService.salvar(altitudeInvalida));

        verify(aeroportoRepository, never()).save(any());
    }
}