package com.rbcfilmes.recomendador.dto;

import com.rbcfilmes.recomendador.model.Movie;
import com.rbcfilmes.recomendador.model.Genre;
import com.rbcfilmes.recomendador.model.Keyword;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimilaridadeDTO {
    private String original_title;     // Título original do filme
    private String overview;           // Resumo do filme
    private String release_date;       // Data de lançamento
    private Double popularity;         // Popularidade do filme
    private Double vote_average;       // Média de votos
    private double global_similarity;  // Similaridade global calculada
    private Double id;                 // ID do filme
    private List<Genre> genres;
    private List<Keyword> Keywords;

    // Construtor a partir de um Movie e a similaridade calculada
    public static SimilaridadeDTO fromFilme(Movie movie, double global_similarity) {
        return new SimilaridadeDTO(
                movie.getOriginal_title(),
                movie.getOverview(),
                movie.getRelease_date(),
                movie.getPopularity(),
                movie.getVote_average(),
                global_similarity,
                movie.getId(),
                movie.getGenres(),
                movie.getKeywords()
        );
    }
}