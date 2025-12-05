package com.tp.aeroportos.controller;

import com.tp.aeroportos.exception.AeroportoNaoEncontradoException;
import com.tp.aeroportos.model.Aeroporto;
import com.tp.aeroportos.service.AeroportoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aeroportos")
public class AeroportoController {

    private final AeroportoService aeroportoService;

    public AeroportoController(AeroportoService aeroportoService) {
        this.aeroportoService = aeroportoService;
    }

    @GetMapping
    public ResponseEntity<List<Aeroporto>> listarTodos() {
        return ResponseEntity.ok(aeroportoService.listarTodos());
    }

    @GetMapping("/{iata}")
    public ResponseEntity<Aeroporto> buscarPorIata(@PathVariable String iata) {
        return aeroportoService.buscarPorIata(iata)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Aeroporto aeroporto) {
        try {
            Aeroporto novoAeroporto = aeroportoService.salvar(aeroporto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAeroporto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{iata}")
    public ResponseEntity<?> atualizar(@PathVariable String iata, @Valid @RequestBody Aeroporto aeroporto) {
        try {
            return ResponseEntity.ok(aeroportoService.atualizar(iata, aeroporto));
        } catch (AeroportoNaoEncontradoException e) {
            return ResponseEntity.notFound().build(); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); 
        }
    }

    @DeleteMapping("/{iata}")
    public ResponseEntity<Void> deletar(@PathVariable String iata) {
        try {
            aeroportoService.deletar(iata);
            return ResponseEntity.noContent().build();
        } catch (AeroportoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}