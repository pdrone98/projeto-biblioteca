package com.appBiblioteca.appBiblioteca.LivroServiceTest;

import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.repository.LivroRepository;
import com.appBiblioteca.appBiblioteca.service.LivroService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    public LivroService livroService;

    @Test
    public void buscaLivroPorIdTest(){
        Livro livro = new Livro();

        livro.setId(1);
        livro.setIsbn("1234");
        livro.setTitulo("Teste Titulo");
        livro.setCategoria("Ficção");
        livro.setAutor("AutorTeste");
        livro.setDataPublicacao(LocalDate.of(2020,01,01));

        when(livroRepository.findById(1)).thenReturn(Optional.of(livro));

        Optional<Livro> encontrou = livroService.buscarLivroPorId(1);

        assertThat(encontrou).isPresent();

    }

}
