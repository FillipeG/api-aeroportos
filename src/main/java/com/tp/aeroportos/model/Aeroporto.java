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
@NoArgsConstructor 
@AllArgsConstructor 
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

    
    public static String obterIsoPais(String nomePais) {
        if (nomePais == null || nomePais.trim().isEmpty()) {
            return "XX";
        }
        
        String nomeLimpo = nomePais.trim();

        return switch (nomeLimpo) {
            
            case "Brazil", "Brasil" -> "BR";
            case "United States", "USA", "Estados Unidos" -> "US";
            case "Canada" -> "CA";
            case "Mexico" -> "MX";
            case "Argentina" -> "AR";
            case "Chile" -> "CL";
            case "Colombia" -> "CO";
            case "Peru" -> "PE";
            
            
            case "Germany", "Alemanha" -> "DE";
            case "United Kingdom", "Reino Unido", "Great Britain" -> "GB";
            case "France", "França" -> "FR";
            case "Italy", "Itália" -> "IT";
            case "Spain", "Espanha" -> "ES";
            case "Portugal" -> "PT";
            case "Netherlands", "Holanda" -> "NL";
            case "Belgium", "Bélgica" -> "BE";
            case "Switzerland", "Suíça" -> "CH";
            case "Sweden", "Suécia" -> "SE";
            case "Norway", "Noruega" -> "NO";
            case "Russia", "Rússia" -> "RU";
            
            
            case "Japan", "Japão" -> "JP";
            case "China" -> "CN";
            case "India", "Índia" -> "IN";
            case "South Korea", "Coreia do Sul" -> "KR";
            case "Australia", "Austrália" -> "AU";
            case "New Zealand", "Nova Zelândia" -> "NZ";
            case "Philippines", "Filipinas" -> "PH";
            
            
            case "South Africa", "África do Sul" -> "ZA";
            case "Saudi Arabia", "Arábia Saudita" -> "SA";
            case "Turkey", "Turquia" -> "TR";
            case "Israel" -> "IL";
            
            
            default -> {
                if (nomeLimpo.length() >= 2) {
                    yield nomeLimpo.substring(0, 2).toUpperCase();
                }
                yield "XX";
            }
        };
    }
}