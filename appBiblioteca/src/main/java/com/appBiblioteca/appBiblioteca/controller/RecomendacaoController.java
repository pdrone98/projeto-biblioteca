package com.appBiblioteca.appBiblioteca.controller;

import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.service.RecomendacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/biblioteca/recomendar")
public class RecomendacaoController {

    @Autowired
    public RecomendacaoService recomendacaoService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Livro>> recomendarLivros(@PathVariable Integer idUsuario){
        List<Livro> recomendacoes = recomendacaoService.recomendarLivros(idUsuario);
        return ResponseEntity.ok(recomendacoes);
    }

}
