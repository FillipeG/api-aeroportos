package com.tp.aeroportos.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AeroportoTest {

    @Test
    @DisplayName("Deve converter pés para metros corretamente")
    void deveConverterPesParaMetros() {
        double pes = 1000.0;
        double esperadoMetros = 304.8;

        double resultado = Aeroporto.converterPesParaMetros(pes);

        assertEquals(esperadoMetros, resultado, 0.01);
    }
}