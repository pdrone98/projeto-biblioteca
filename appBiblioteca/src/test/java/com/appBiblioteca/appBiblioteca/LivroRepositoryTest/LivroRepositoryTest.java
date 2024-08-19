package com.appBiblioteca.appBiblioteca.LivroRepositoryTest;

import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.repository.LivroRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class LivroRepositoryTest {

    @Autowired
    private LivroRepository livroRepository;

    @Test
    public void testeBuscaLivroPorId(){
        Livro livro = new Livro();
        livro.setId(1);
        livro.setIsbn("1234");
        livro.setTitulo("Teste Titulo");
        livro.setCategoria("Ficção");
        livro.setAutor("AutorTeste");
        livro.setDataPublicacao(LocalDate.of(2020,01,01));

        livroRepository.save(livro);

        Optional<Livro> encontrou = livroRepository.findById(1);

        assertThat(encontrou).isNotNull();
    }

    @Test
    public void testDeletaLivroPorId(){
        Livro livro = new Livro();
        livro.setId(1);
        livro.setIsbn("1234");
        livro.setTitulo("Teste Titulo");
        livro.setCategoria("Ficção");
        livro.setAutor("AutorTeste");
        livro.setDataPublicacao(LocalDate.of(2020,01,01));

        livroRepository.save(livro);

        livroRepository.deleteById(1);

        assertThat(livro.getId().equals(null));

    }

}
