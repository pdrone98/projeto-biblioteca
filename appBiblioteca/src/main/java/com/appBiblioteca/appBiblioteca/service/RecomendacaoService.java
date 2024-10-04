package com.appBiblioteca.appBiblioteca.service;

import com.appBiblioteca.appBiblioteca.exceptions.UsuarioNotFoundException;
import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.LivenessState;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecomendacaoService {

    @Autowired
    EmprestimoService emprestimoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    LivroService livroService;

    public List<Livro> recomendarLivros (Integer idUsuario){
        Set<String> categoriasInteresse = emprestimoService.obterCategoriasDoUsuario(idUsuario);
        List<Livro> recomendacoes = new ArrayList<>();

        Optional<Usuario> usuarioEmp = usuarioService.buscarUsuarioPorId(idUsuario);
        if(!usuarioEmp.isPresent()){
            throw new UsuarioNotFoundException("Usuario não encontrado");
        } else if (usuarioEmp.get().getListaEmprestimos().isEmpty()) {
            throw new RuntimeException("Usuario ainda não realizou nenhum empréstimo ");
        }

        for (String categoria: categoriasInteresse){
            List<Livro> livrosNaoEmprestados = livroService.buscarLivrosPorCategoriaNaoEmprestadosPeloUsuario(categoria,idUsuario);
            recomendacoes.addAll(livrosNaoEmprestados);
        }
        return recomendacoes;
    }
}
