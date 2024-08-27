package com.appBiblioteca.appBiblioteca.LivroServiceTest;

import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.model.Usuario;
import com.appBiblioteca.appBiblioteca.repository.LivroRepository;
import com.appBiblioteca.appBiblioteca.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LivroServiceTest {

    private static final Integer ID = 1;
    private static final String ISBN = "123456789";
    private static final String AUTOR = "Autor Teste";
    private static final String TITULO = "Titulo Teste";
    private static final String CATEGORIA = "Categoria Teste";
    private static final LocalDate DATA_PUBLICACAO = LocalDate.of(2000,01,01);

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    public LivroService livroService;

    private Livro livro;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        livro = new Livro();
        livro.setId(ID);
        livro.setAutor(AUTOR);
        livro.setIsbn(ISBN);
        livro.setTitulo(TITULO);
        livro.setDataPublicacao(DATA_PUBLICACAO);
        livro.setCategoria(CATEGORIA);
    }

    @Test
    public void testBuscaLivroPorId(){

        //configura o mock para retornar o livro quando findById é chamado
        when(livroRepository.findById(ID)).thenReturn(Optional.of(livro));

        Optional<Livro> encontrou = livroService.buscarLivroPorId(ID);

        //verificar se o livro encontrado é igual ao livro mockado
        assertTrue(encontrou.isPresent(), "Livro nao encontrado");
        assertThat(encontrou.get().getId()).isEqualTo(ID);
        assertThat(encontrou.get().getCategoria()).isEqualTo(CATEGORIA);
        assertThat(encontrou.get().getAutor()).isEqualTo(AUTOR);
        assertThat(encontrou.get().getIsbn()).isEqualTo(ISBN);
        assertThat(encontrou.get().getTitulo()).isEqualTo(TITULO);
        assertThat(encontrou.get().getDataPublicacao()).isEqualTo(DATA_PUBLICACAO);

        //verifica se o metodo findById foi chamado
        verify(livroRepository).findById(ID);
    }
    @Test
    public void testAlteracaoLivro(){
        //configura o mock para retornar o livro quando findById é chamado
        when(livroRepository.findById(ID)).thenReturn(Optional.of(livro));

        Optional<Livro> livroEncontrado = livroService.buscarLivroPorId(ID);

        livroEncontrado.get().setId(2);
        livroEncontrado.get().setAutor("TESTE");
        livroEncontrado.get().setCategoria("TESTE");
        livroEncontrado.get().setTitulo("TESTE");
        livroEncontrado.get().setIsbn("1122334455");
        livroEncontrado.get().setDataPublicacao(LocalDate.of(2001,02,02));


        assertTrue(livroEncontrado.isPresent(), "Livro nao encontrado");
        assertFalse(livroEncontrado.get().getCategoria().equals(CATEGORIA));
        assertFalse(livroEncontrado.get().getAutor().equals(AUTOR));
        assertFalse(livroEncontrado.get().getId().equals(ID));
        assertFalse(livroEncontrado.get().getDataPublicacao().equals(DATA_PUBLICACAO));
        assertFalse(livroEncontrado.get().getIsbn().equals(ISBN));
        assertFalse(livroEncontrado.get().getTitulo().equals(TITULO));

        verify(livroRepository).findById(ID);

    }

    @Test
    public void testNaoRetornaLivroQuandoNaoExistir(){
        //Configura o mock para retornar um Optional vazio quando findById é chamado
        when(livroRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<Livro> livroEncontrado = livroService.buscarLivroPorId(ID);

        //verifica se o livro não está presente
        assertTrue(livroEncontrado.isEmpty(),"Livro encontrado nao deveria existir");

        //verifica se o metodo findById foi chamado
        verify(livroRepository).findById(ID);
    }

}
