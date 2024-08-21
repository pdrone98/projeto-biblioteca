package com.appBiblioteca.appBiblioteca.service;

import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<Livro> buscarTodosLivros() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarLivroPorId(Integer id) {
        return livroRepository.findById(id);
    }

    public Livro salvarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public void deletarLivro(Integer id) {
        livroRepository.deleteById(id);
    }
}
