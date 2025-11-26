package com.tp.aeroportos.controller;

import com.tp.aeroportos.model.Aeroporto;
import com.tp.aeroportos.service.AeroportoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aeroportos") //define o caminho 
public class AeroportoController {

    private final AeroportoService aeroportoService;

    public AeroportoController(AeroportoService aeroportoService) {
        this.aeroportoService = aeroportoService;
    }

    //obter todos
    @GetMapping
    public ResponseEntity<List<Aeroporto>> listarTodos() {
        return ResponseEntity.ok(aeroportoService.listarTodos());
    }

    //obter por IATA
    @GetMapping("/{iata}")
    public ResponseEntity<Aeroporto> buscarPorIata(@PathVariable String iata) {
        return aeroportoService.buscarPorIata(iata)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //adicionar novo
    @PostMapping
    public ResponseEntity<Aeroporto> cadastrar(@Valid @RequestBody Aeroporto aeroporto) {
        Aeroporto novoAeroporto = aeroportoService.salvar(aeroporto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAeroporto);
    }

    //atualizar existente
    @PutMapping("/{iata}")
    public ResponseEntity<Aeroporto> atualizar(@PathVariable String iata, @Valid @RequestBody Aeroporto aeroporto) {
        try {
            return ResponseEntity.ok(aeroportoService.atualizar(iata, aeroporto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //excluir
    @DeleteMapping("/{iata}")
    public ResponseEntity<Void> deletar(@PathVariable String iata) {
        try {
            aeroportoService.deletar(iata);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}