package com.rbcfilmes.recomendador.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.rbcfilmes.recomendador.model.Movie;
import com.rbcfilmes.recomendador.model.Genre;
import com.rbcfilmes.recomendador.model.Keyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j // Lombok: gera automaticamente o logger (log)
@Service
public class ReadCSV {

    // Usado para converter strings JSON em objetos Java (Genre, Keyword)
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Lê o arquivo filmes.csv da pasta resources/dados e transforma cada linha em um objeto Movie.
     * Em caso de erro na leitura ou conversão, o erro é logado.
     */
    public List<Movie> carregarFilmes() {
        List<Movie> movies = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("dados/filmes.csv")))) {

            reader.readNext(); // Ignora a primeira linha (cabeçalho)
            String[] linha;

            // Lê cada linha do CSV, transforma em um objeto Movie e adiciona à lista
            while ((linha = reader.readNext()) != null) {
                Movie movie = new Movie(
                        parseDouble(linha[0]),          // budget
                        parseGenres(linha[1]),          // genres (lista de objetos Genre)
                        linha[2],                       // homepage
                        parseDouble(linha[3]),          // id
                        parseKeywords(linha[4]),        // keywords (lista de objetos Keyword)
                        linha[5],                       // original_language
                        linha[6],                       // original_title
                        linha[7],                       // overview
                        parseDouble(linha[8]),          // popularity
                        parseList(linha[9]),            // production_companies
                        parseList(linha[10]),           // production_countries
                        linha[11],                      // release_date
                        parseDouble(linha[12]),         // revenue
                        parseDouble(linha[13]),         // runtime
                        parseList(linha[14]),           // spoken_languages
                        linha[15],                      // status
                        linha[16],                      // tagline
                        linha[17],                      // title
                        parseDouble(linha[18]),         // vote_average
                        parseInt(linha[19])             // vote_count
                );
                movies.add(movie); // Adiciona o movie à lista
            }
        } catch (Exception e) {
            // Loga erro de leitura do CSV
            log.error("Erro ao ler o arquivo CSV de movies", e);
        }
        return movies;
    }

    /**
     * Converte a string JSON da coluna "genres" em uma lista de objetos Genre.
     * Em caso de erro, retorna lista vazia.
     */
    private List<Genre> parseGenres(String value) {
        try {
            return objectMapper.readValue(value, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.warn("Erro ao converter gêneros: {}", value, e);
            return Collections.emptyList();
        }
    }

    /**
     * Converte a string JSON da coluna "keywords" em uma lista de objetos Keyword.
     * Em caso de erro, retorna lista vazia.
     */
    private List<Keyword> parseKeywords(String value) {
        try {
            return objectMapper.readValue(value, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.warn("Erro ao converter palavras-chave: {}", value, e);
            return Collections.emptyList();
        }
    }

    /**
     * Converte uma string separada por vírgulas em uma lista de strings.
     * Se for nula ou vazia, retorna lista vazia.
     */
    private List<String> parseList(String value) {
        return (value == null || value.isBlank())
                ? Collections.emptyList()
                : Arrays.asList(value.split(","));
    }

    /**
     * Converte string em Double.
     * Retorna 0.0 caso a string seja nula, vazia ou inválida.
     */
    private Double parseDouble(String value) {
        try {
            return (value == null || value.isBlank()) ? 0.0 : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.warn("Erro ao converter para Double: {}", value, e);
            return 0.0;
        }
    }

    /**
     * Converte string em Integer.
     * Retorna 0 caso a string seja nula, vazia ou inválida.
     */
    private Integer parseInt(String value) {
        try {
            return (value == null || value.isBlank()) ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Erro ao converter para Integer: {}", value, e);
            return 0;
        }
    }
}
