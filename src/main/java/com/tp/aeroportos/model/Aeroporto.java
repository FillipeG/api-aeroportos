package com.tp.aeroportos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor //construtor vazio 
@AllArgsConstructor //construtor completo
@Entity
@Table(name = "aeroportos")
public class Aeroporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeroporto")
    private Long id;

    @NotBlank(message = "O nome do aeroporto é obrigatório")
    @Column(name = "nome_aeroporto", nullable = false)
    private String nome;

    @NotBlank(message = "O código IATA é obrigatório")
    @Size(min = 3, max = 3, message = "O código IATA deve ter exatamente 3 letras")
    @Column(name = "codigo_iata", unique = true, length = 3, nullable = false)
    private String iata;

    @NotBlank(message = "A cidade é obrigatória")
    @Column(nullable = false)
    private String cidade;

    @NotBlank(message = "O código do país ISO é obrigatório")
    @Size(min = 2, max = 2, message = "O código do país deve ter exatamente 2 letras")
    @Column(name = "codigo_pais_iso", length = 2, nullable = false)
    private String codigoPais;

    @NotNull(message = "A latitude é obrigatória")
    private Double latitude;

    @NotNull(message = "A longitude é obrigatória")
    private Double longitude;

    @NotNull(message = "A altitude é obrigatória")
    private Double altitude;

    public static double converterPesParaMetros(double pes) {
        return pes * 0.3048;
    }
}