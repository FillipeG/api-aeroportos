package com.tp.aeroportos.config;

import com.tp.aeroportos.model.Aeroporto;
import com.tp.aeroportos.repository.AeroportoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration
public class CargaDados implements CommandLineRunner {

    private final AeroportoRepository aeroportoRepository;

    public CargaDados(AeroportoRepository aeroportoRepository) {
        this.aeroportoRepository = aeroportoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (aeroportoRepository.count() > 0) {
            System.out.println("O banco de dados já contém dados. Carga ignorada.");
            return;
        }

        System.out.println("Iniciando a carga de dados do arquivo airports.csv...");

        try (InputStream inputStream = getClass().getResourceAsStream("/airports.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados.length >= 9) {
                    try {
                        Aeroporto aeroporto = new Aeroporto();
                        
                        // [1]=Nome
                        String nome = limparTexto(dados[1]);
                        if (nome.isEmpty()) nome = "Aeroporto sem nome";
                        aeroporto.setNome(nome);

                        // [2]=Cidade (Tratamento para evitar erro de validação)
                        String cidade = limparTexto(dados[2]);
                        if (cidade.isEmpty()) cidade = "Localização Desconhecida"; 
                        aeroporto.setCidade(cidade);
                        
                        // [3]=País (Tratamento para pegar sigla de 2 letras)
                        String nomePais = limparTexto(dados[3]);
                        if (nomePais != null && nomePais.length() >= 2) {
                            aeroporto.setCodigoPais(nomePais.substring(0, 2).toUpperCase());
                        } else {
                            aeroporto.setCodigoPais("XX");
                        }

                        // [4]=IATA
                        String iata = limparTexto(dados[4]);
                        if (iata == null || iata.length() != 3 || iata.equals("\\N")) {
                            continue; // IATA é chave única e obrigatória, se não tiver, pula mesmo.
                        }
                        aeroporto.setIata(iata);

                        // [6,7,8]=Lat, Lon, Alt
                        aeroporto.setLatitude(Double.parseDouble(dados[6]));
                        aeroporto.setLongitude(Double.parseDouble(dados[7]));
                        aeroporto.setAltitude(Double.parseDouble(dados[8]));

                        aeroportoRepository.save(aeroporto);
                    } catch (Exception e) {
                        // Log simplificado para não sujar o terminal
                        // System.out.println("Linha ignorada: " + linha);
                    }
                }
            }
            System.out.println("Carga de dados finalizada com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro fatal na carga de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String limparTexto(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "").trim();
    }
}