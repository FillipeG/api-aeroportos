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
            System.out.println("O banco de dados ja contem dados. Carga ignorada.");
            return;
        }

        System.out.println("Iniciando a carga de dados do arquivo airports.csv...");

        try (InputStream inputStream = getClass().getResourceAsStream("/airports.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String linha;
            
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados.length >= 9) {
                    try {
                        Aeroporto aeroporto = new Aeroporto();
                        
                        String nome = limparTexto(dados[1]);
                        aeroporto.setNome(nome.isEmpty() ? "Aeroporto Sem Nome" : nome);

                        String cidade = limparTexto(dados[2]);
                        aeroporto.setCidade(cidade.isEmpty() ? "Desconhecida" : cidade);
                        
                        String nomePais = limparTexto(dados[3]);
                        aeroporto.setCodigoPais(Aeroporto.obterIsoPais(nomePais));

                        String iata = limparTexto(dados[4]);
                        if (iata == null || iata.length() != 3 || iata.equals("\\N")) {
                            continue; 
                        }
                        aeroporto.setIata(iata);

                        aeroporto.setLatitude(Double.parseDouble(dados[6]));
                        aeroporto.setLongitude(Double.parseDouble(dados[7]));
                        aeroporto.setAltitude(Double.parseDouble(dados[8]));

                        aeroportoRepository.save(aeroporto);
                    } catch (Exception e) {
                        System.out.println("Erro ao processar linha: " + linha);
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