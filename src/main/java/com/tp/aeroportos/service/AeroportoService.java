package com.tp.aeroportos.service;

import com.tp.aeroportos.exception.AeroportoNaoEncontradoException;
import com.tp.aeroportos.model.Aeroporto;
import com.tp.aeroportos.repository.AeroportoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AeroportoService {

    private final AeroportoRepository aeroportoRepository;

    public AeroportoService(AeroportoRepository aeroportoRepository) {
        this.aeroportoRepository = aeroportoRepository;
    }

    public List<Aeroporto> listarTodos() {
        return aeroportoRepository.findAll();
    }

    public Optional<Aeroporto> buscarPorIata(String iata) {
        return aeroportoRepository.findByIata(iata);
    }

    @Transactional
    public Aeroporto salvar(Aeroporto aeroporto) {
        validarDados(aeroporto); 

        if (aeroportoRepository.findByIata(aeroporto.getIata()).isPresent()) {
            throw new IllegalArgumentException("Já existe um aeroporto cadastrado com o código IATA: " + aeroporto.getIata());
        }

        return aeroportoRepository.save(aeroporto);
    }

    @Transactional
    public Aeroporto atualizar(String iata, Aeroporto dadosAtualizados) {
        Aeroporto aeroportoExistente = aeroportoRepository.findByIata(iata)
                .orElseThrow(() -> new AeroportoNaoEncontradoException(iata)); 

        validarDados(dadosAtualizados); 

        aeroportoExistente.setNome(dadosAtualizados.getNome());
        aeroportoExistente.setCidade(dadosAtualizados.getCidade());
        aeroportoExistente.setCodigoPais(dadosAtualizados.getCodigoPais());
        aeroportoExistente.setLatitude(dadosAtualizados.getLatitude());
        aeroportoExistente.setLongitude(dadosAtualizados.getLongitude());
        aeroportoExistente.setAltitude(dadosAtualizados.getAltitude());

        return aeroportoRepository.save(aeroportoExistente);
    }

    @Transactional
    public void deletar(String iata) {
        Aeroporto aeroporto = aeroportoRepository.findByIata(iata)
                .orElseThrow(() -> new AeroportoNaoEncontradoException(iata)); 
        aeroportoRepository.delete(aeroporto);
    }

    private void validarDados(Aeroporto aeroporto) {
        if (aeroporto.getIata() == null || aeroporto.getIata().length() != 3) {
            throw new IllegalArgumentException("O código IATA deve ter exatamente 3 letras.");
        }
        
        if (aeroporto.getAltitude() != null && aeroporto.getAltitude() < 0) {
            throw new IllegalArgumentException("A altitude não pode ser negativa.");
        }
    }
}