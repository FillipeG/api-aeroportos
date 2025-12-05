package com.tp.aeroportos.exception;

public class AeroportoNaoEncontradoException extends RuntimeException {
    public AeroportoNaoEncontradoException(String iata) {
        super("Aeroporto não encontrado com o código IATA: " + iata);
    }
}