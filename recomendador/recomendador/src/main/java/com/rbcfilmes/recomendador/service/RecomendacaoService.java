package com.rbcfilmes.recomendador.service;

import com.rbcfilmes.recomendador.dto.SimilaridadeDTO;
import com.rbcfilmes.recomendador.model.Movie;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecomendacaoService {

    // Pesos atribuídos a cada atributo no cálculo da similaridade global
    @Value("${rbc.peso.generos}")
    private double pesoGeneros;

    @Value("${rbc.peso.popularidade}")
    private double pesoPopularidade;

    @Value("${rbc.peso.votos}")
    private double pesoVotos;

    @Value("${rbc.peso.titulo}")
    private double pesoTitulo;

    @Value("${rbc.peso.palavras}")
    private double pesoPalavras;

    /**
     * Calcula a similaridade global entre dois filmes, combinando as similaridades
     * individuais de cada atributo ponderadas por seus respectivos pesos.
     */
    public double calcularSimilaridade(Movie movie1, Movie movie2) {
        double simGeneros = calcularSimilaridadeListas(movie1.getGenres(), movie2.getGenres());
        double simPopularidade = calcularSimilaridadePopularidade(movie1, movie2);
        double simVotos = calcularSimilaridadeVotos(movie1, movie2);
        double simTitulo = calcularSimilaridadeTitulo(movie1, movie2);
        double simPalavras = calcularSimilaridadeListas(movie1.getKeywords(), movie2.getKeywords());

        return (pesoGeneros * simGeneros)
                + (pesoPopularidade * simPopularidade)
                + (pesoVotos * simVotos)
                + (pesoTitulo * simTitulo)
                + (pesoPalavras * simPalavras);
    }

    /**
     * Calcula a similaridade entre duas listas (por exemplo, gêneros ou palavras-chave).
     * Utiliza a razão entre a interseção e o tamanho da maior lista como métrica.
     */
    private <T> double calcularSimilaridadeListas(List<T> lista1, List<T> lista2) {
        if (lista1 == null || lista2 == null || lista1.isEmpty() || lista2.isEmpty()) return 0.0;

        long emComum = lista1.stream().filter(lista2::contains).count();
        return (double) emComum / Math.max(lista1.size(), lista2.size());
    }

    /**
     * Calcula a similaridade de popularidade entre dois filmes.
     * A diferença é normalizada considerando o valor máximo presente no conjunto de dados (875.5).
     */
    private double calcularSimilaridadePopularidade(Movie movie1, Movie movie2) {
        return 1 - Math.abs(movie1.getPopularity() - movie2.getPopularity()) / 875.5;
    }

    /**
     * Calcula a similaridade da média de votos entre dois filmes.
     * A escala de normalização vai de 0 a 10.
     */
    private double calcularSimilaridadeVotos(Movie movie1, Movie movie2) {
        return 1 - Math.abs(movie1.getVote_average() - movie2.getVote_average()) / 10.0;
    }

    /**
     * Calcula a similaridade entre os títulos originais dos filmes
     * usando a distância de Levenshtein, normalizada pelo comprimento do maior título.
     */
    private double calcularSimilaridadeTitulo(Movie movie1, Movie movie2) {
        String titulo1 = movie1.getOriginal_title().toLowerCase();
        String titulo2 = movie2.getOriginal_title().toLowerCase();

        LevenshteinDistance levenshtein = new LevenshteinDistance();
        int distancia = levenshtein.apply(titulo1, titulo2);
        int comprimentoMaximo = Math.max(titulo1.length(), titulo2.length());

        return comprimentoMaximo == 0 ? 1.0 : 1.0 - (double) distancia / comprimentoMaximo;
    }

    /**
     * Retorna uma lista com os 5 filmes mais semelhantes ao filme-base informado,
     * ordenados da maior para a menor similaridade global.
     */
    public List<SimilaridadeDTO> recomendar(Movie movieBase, List<Movie> todosMovies) {
        return todosMovies.stream()
                .filter(f -> !f.getId().equals(movieBase.getId())) // Ignora o próprio filme-base
                .map(f -> SimilaridadeDTO.fromFilme(f, calcularSimilaridade(movieBase, f)))
                .sorted(Comparator.comparingDouble(SimilaridadeDTO::getGlobal_similarity).reversed())
                .limit(5)
                .toList();
    }
}
