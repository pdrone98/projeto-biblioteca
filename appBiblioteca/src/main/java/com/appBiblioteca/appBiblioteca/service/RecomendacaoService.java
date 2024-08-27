package com.appBiblioteca.appBiblioteca.service;

import com.appBiblioteca.appBiblioteca.model.Livro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.LivenessState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecomendacaoService {

    @Autowired
    EmprestimoService emprestimoService;

    @Autowired
    LivroService livroService;

    public List<Livro> recomendarLivros (Integer idUsuario){
        Set<String> categoriasInteresse = emprestimoService.obterCategoriasDoUsuario(idUsuario);
        List<Livro> recomendacoes = new ArrayList<>();

        for (String categoria: categoriasInteresse){
            List<Livro> livrosNaoEmprestados = livroService.buscarLivrosPorCategoriaNaoEmprestadosPeloUsuario(categoria,idUsuario);
            recomendacoes.addAll(livrosNaoEmprestados);
        }
        return recomendacoes;
    }
}
