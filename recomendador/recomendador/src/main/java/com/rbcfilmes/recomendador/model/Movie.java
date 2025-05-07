package com.rbcfilmes.recomendador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Classe baseada nas colunas do arquivo CSV utilizado na aplicação de Raciocínio Baseado em Casos (RBC)
public class Movie {
    private Double budget;                     // orçamento do filme (coluna: budget)
    private List<Genre> genres;
    private String homepage;                   // link da página oficial (coluna: homepage)
    private Double id;                         // ID único do filme (coluna: id)
    private List<Keyword> Keywords;
    private String original_language;          // idioma original (coluna: original_language)
    private String original_title;             // título original (coluna: original_title)
    private String overview;                   // resumo da história (coluna: overview)
    private Double popularity;                 // popularidade do filme (coluna: popularity)
    private List<String> production_companies;
    private List<String> production_countries;
    private String release_date;               // data de lançamento (coluna: release_date)
    private Double revenue;                    // receita gerada (coluna: revenue)
    private Double runtime;                    // duração em minutos (coluna: runtime)
    private List<String> spoken_languages;
    private String status;                     // status do filme (coluna: status)
    private String tagline;                    // frase de marketing (coluna: tagline)
    private String title;                      // título do filme (coluna: title)
    private Double vote_average;               // média de votos (coluna: vote_average)
    private Integer vote_count;                // número de votos (coluna: vote_count)
}
