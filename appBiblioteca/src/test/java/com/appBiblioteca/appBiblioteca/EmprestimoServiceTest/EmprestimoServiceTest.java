package com.appBiblioteca.appBiblioteca.EmprestimoServiceTest;

import com.appBiblioteca.appBiblioteca.exceptions.LivroNotFoundException;
import com.appBiblioteca.appBiblioteca.model.Emprestimo;
import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.model.Usuario;
import com.appBiblioteca.appBiblioteca.repository.EmprestimoRepository;
import com.appBiblioteca.appBiblioteca.repository.LivroRepository;
import com.appBiblioteca.appBiblioteca.repository.UsuarioRepository;
import com.appBiblioteca.appBiblioteca.service.EmprestimoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmprestimoServiceTest {

    private static final Integer ID = 1;
    private static final Integer LIVRO_ID = 1;
    private static final Integer USUARIO_ID = 1;
    private static final String STATUS = "Vigente";
    private static final LocalDate DATA_EMPRESTIMO = LocalDate.now().minusDays(10);
    private static final LocalDate DATA_DEVOLUCAO = LocalDate.now().plusDays(20);

    @InjectMocks
    EmprestimoService emprestimoService;

    @Mock
    EmprestimoRepository emprestimoRepository;

    @Mock
    LivroRepository livroRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    private Emprestimo emprestimo;
    private Usuario usuario;
    private Livro livro;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        //configurar livro
        livro = new Livro();
        livro.setId(LIVRO_ID);
        livro.setTitulo("Teste Livro");
        livro.setAutor("Teste Autor");
        livro.setIsbn("112233");
        livro.setDataPublicacao(LocalDate.of(2000,01,01));
        livro.setCategoria("TESTE");
        livro.setListaLivrosEmprestados(new ArrayList<>());

        //configurar usuario
        usuario = new Usuario();
        usuario.setId(USUARIO_ID);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("email@email.com");
        usuario.setDataCadastro(LocalDate.of(2002,02,02));
        usuario.setTelefone("11224466");
        usuario.setListaEmprestimos(new ArrayList<>());

        //configurar emprestimo ativo
        emprestimo = new Emprestimo();
        emprestimo.setIdEmprestimo(ID);
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(DATA_EMPRESTIMO);
        emprestimo.setDataDevolucao(DATA_DEVOLUCAO);
        emprestimo.setStatus(STATUS);

        livro.getListaLivrosEmprestados().add(emprestimo);

        //configurar mock para o repositorio
        when(livroRepository.findById(LIVRO_ID)).thenReturn(Optional.of(livro));
        when(usuarioRepository.findById(USUARIO_ID)).thenReturn(Optional.of(usuario));
        when(emprestimoRepository.findById(ID)).thenReturn(Optional.of(emprestimo));

    }

    @Test
    public void testLancaExceptionQuandoLivroEstaEmprestado(){

        assertThatThrownBy(()-> emprestimoService.criarEmprestimo(USUARIO_ID, LIVRO_ID, LocalDate.now()))
                .isInstanceOf(LivroNotFoundException.class)
                .hasMessage("O livro escolhido já está emprestado, escolha outro livro.");
    }
}
