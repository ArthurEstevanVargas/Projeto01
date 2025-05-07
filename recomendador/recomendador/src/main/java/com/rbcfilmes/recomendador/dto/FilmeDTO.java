package com.rbcfilmes.recomendador.dto;

import com.rbcfilmes.recomendador.model.Genre;
import com.rbcfilmes.recomendador.model.Keyword;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilmeDTO {
    private Double id;               // ID do filme
    private String title;            // Título do filme
    private String original_title;   // Título original do filme
    private String overview;         // Resumo do filme
    private String release_date;     // Data de lançamento
    private Double popularity;       // Popularidade do filme
    private Double vote_average;     // Média de votos
    private List<Genre> genres;
    private List<Keyword> Keywords;
}