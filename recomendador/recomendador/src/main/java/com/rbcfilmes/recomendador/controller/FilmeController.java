package com.rbcfilmes.recomendador.controller;

import com.rbcfilmes.recomendador.dto.FilmeDTO;
import com.rbcfilmes.recomendador.dto.SimilaridadeDTO;
import com.rbcfilmes.recomendador.model.Movie;
import com.rbcfilmes.recomendador.service.ReadCSV;
import com.rbcfilmes.recomendador.service.RecomendacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final RecomendacaoService recomendacaoService;
    private final List<Movie> movies;

    // Construtor carrega os movies do CSV na inicialização do controller
    public FilmeController(RecomendacaoService recomendacaoService, ReadCSV readCSV) {
        this.recomendacaoService = recomendacaoService;
        this.movies = readCSV.carregarFilmes(); // Leitura única, evita reprocessar a cada requisição
    }

    /**
     * Endpoint: GET /movies/recomendar?titulo=TITULO_DO_FILME
     * Função: retorna os 5 movies mais similares com base no título informado.
     * Utiliza Raciocínio Baseado em Casos (RBC) com base nos atributos:
     * gêneros, popularidade e média de votos.
     * <p>
     * Exemplo front-end:
     * fetch("/movies/recomendar?titulo=Avatar").then(res => res.json())
     */
    @GetMapping("/recomendar")
    public ResponseEntity<List<SimilaridadeDTO>> recomendar(@RequestParam String titulo) {
        // Busca o filme base pelo título original (case-insensitive)
        Movie base = movies.stream().filter(f -> f.getOriginal_title().equalsIgnoreCase(titulo)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie não encontrado"));
        // Aplica RBC para encontrar os movies mais similares
        List<SimilaridadeDTO> recomendados = recomendacaoService.recomendar(base, movies);
        return ResponseEntity.ok(recomendados);
    }

    /**
     * Endpoint: GET /movies
     * Função: retorna todos os movies disponíveis com os dados mais importantes para o front-end.
     * Exemplo front-end:
     * fetch("/movies").then(res => res.json())
     */
    @GetMapping
    public ResponseEntity<List<FilmeDTO>> listarFilmes() {
        List<FilmeDTO> filmesDTO = movies.stream().map(this::converterParaDTO).collect(Collectors.toList());
        return ResponseEntity.ok(filmesDTO);
    }

    // Metodo auxiliar para converter Movie em FilmeDTO
    private FilmeDTO converterParaDTO(Movie f) {
        return new FilmeDTO(f.getId(), f.getTitle(), f.getOriginal_title(), f.getOverview(), f.getRelease_date(), f.getPopularity(), f.getVote_average(), f.getGenres(), f.getKeywords());
    }
}