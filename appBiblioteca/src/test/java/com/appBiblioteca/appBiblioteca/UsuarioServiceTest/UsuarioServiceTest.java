package com.appBiblioteca.appBiblioteca.UsuarioServiceTest;

import com.appBiblioteca.appBiblioteca.model.Usuario;
import com.appBiblioteca.appBiblioteca.repository.UsuarioRepository;
import com.appBiblioteca.appBiblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsuarioServiceTest {

    private static final Integer ID = 1;
    private static final String NOME = "NomeTeste";
    private static final String EMAIL = "teste@email.com";
    private static final String TELEFONE = "44999888777";
    private static final LocalDate DATA_CADASTRO = LocalDate.of(2000,01,01);

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        //configura usuario para ser usado nos testes
        usuario = new Usuario();
        usuario.setId(ID);
        usuario.setEmail(EMAIL);
        usuario.setNome(NOME);
        usuario.setDataCadastro(DATA_CADASTRO);
        usuario.setTelefone(TELEFONE);
    }

    @Test
    public void testBuscaUsuarioPorId(){

        //configura o mock para retornar o usuario quando findById é chamado
        when(usuarioRepository.findById(ID)).thenReturn(Optional.of(usuario));

        //Metodo a ser testado
        Optional<Usuario> usuarioEncontrado = usuarioService.buscarUsuarioPorId(ID);

        //verificar se o usuario encontrado é igual ao usuario mockado
        assertTrue(usuarioEncontrado.isPresent(), "Usuario deve estar presente");
        assertThat(usuarioEncontrado.get()).isEqualTo(usuario);
        assertThat(usuarioEncontrado.get().getId()).isEqualTo(ID);
        assertThat(usuarioEncontrado.get().getNome()).isEqualTo(NOME);
        assertThat(usuarioEncontrado.get().getEmail()).isEqualTo(EMAIL);
        assertThat(usuarioEncontrado.get().getTelefone()).isEqualTo(TELEFONE);
        assertThat(usuarioEncontrado.get().getDataCadastro()).isEqualTo(DATA_CADASTRO);

        //verifica se o metodo findById foi chamado
        verify(usuarioRepository).findById(ID);
    }

    @Test
    public void testNaoRetornaUsuarioQuandoNaoExistir(){
        //Configura o mock para retornar um Optional vazio quando findById é chamado
        when(usuarioRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<Usuario> usuarioEncontrado = usuarioService.buscarUsuarioPorId(ID);

        //verifica se o usuario não está presente
        assertTrue(usuarioEncontrado.isEmpty(),"Usuario encontrado nao deveria existir");

        //verifica se o metodo findById foi chamado
        verify(usuarioRepository).findById(ID);
    }

    @Test
    public void testAlteracaoUsuario(){
        //configura o mock para retornar o usuario quando findById é chamado
        when(usuarioRepository.findById(ID)).thenReturn(Optional.of(usuario));

        Optional<Usuario> usuarioEncontrado = usuarioService.buscarUsuarioPorId(ID);

        //Altera dados do usuario
        usuarioEncontrado.get().setId(2);
        usuarioEncontrado.get().setTelefone("123123123");
        usuarioEncontrado.get().setNome("Nome2");
        usuarioEncontrado.get().setEmail("emailemail@teste.com");
        usuarioEncontrado.get().setDataCadastro(LocalDate.of(2001,02,02));

        //Verifica se os dados são diferentes dos dados mockados
        assertTrue(usuarioEncontrado.isPresent(), "Usuario nao encontrado");
        assertFalse(usuarioEncontrado.get().getEmail().equals(EMAIL));
        assertFalse(usuarioEncontrado.get().getNome().equals(NOME));
        assertFalse(usuarioEncontrado.get().getId().equals(ID));
        assertFalse(usuarioEncontrado.get().getTelefone().equals(TELEFONE));
        assertFalse(usuarioEncontrado.get().getDataCadastro().equals(DATA_CADASTRO));

        verify(usuarioRepository).findById(ID);

    }


}
