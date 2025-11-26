package com.tp.aeroportos.repository;

import com.tp.aeroportos.model.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {
    
   
    Optional<Aeroporto> findByIata(String iata);
    
}