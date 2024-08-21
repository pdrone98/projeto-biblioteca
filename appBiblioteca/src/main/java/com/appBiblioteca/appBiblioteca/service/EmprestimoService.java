package com.appBiblioteca.appBiblioteca.service;

import com.appBiblioteca.appBiblioteca.exceptions.EmprestimoNotFoundException;
import com.appBiblioteca.appBiblioteca.exceptions.LivroNotFoundException;
import com.appBiblioteca.appBiblioteca.model.Emprestimo;
import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.model.Usuario;
import com.appBiblioteca.appBiblioteca.repository.EmprestimoRepository;
import com.appBiblioteca.appBiblioteca.repository.LivroRepository;
import com.appBiblioteca.appBiblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    public List<Emprestimo> buscarTodosEmprestimos() {
        return emprestimoRepository.findAll();
    }

    public Optional<Emprestimo> buscarEmprestimoPorId(Integer id) {
        return emprestimoRepository.findById(id);
    }

    public Emprestimo criarEmprestimo(Integer idUsuario, Integer idLivro, LocalDate dataEmprestimo) {
        Optional<Usuario> usuarioEmp = usuarioRepository.findById(idUsuario);
        Optional<Livro> livroEmp = livroRepository.findById(idLivro);

        if (usuarioEmp.isPresent() && livroEmp.isPresent()) {
            Livro livroEscolhido = livroEmp.get();
            Usuario usuarioEscolhido = usuarioEmp.get();

            if(dataEmprestimo.isAfter(LocalDate.now())){
                throw new RuntimeException("A data do empréstimo não pode ser maior que a data atual.");
            }

            boolean existeEmprestimoAtivo = livroEscolhido.getListaLivrosEmprestados().stream().anyMatch(e -> e.getStatus().equals("Vigente"));
            if(existeEmprestimoAtivo){
                throw new LivroNotFoundException("O livro escolhido já está emprestado, escolha outro livro.");
            }
            //cria emprestimo
            Emprestimo emprestimo = new Emprestimo();

            emprestimo.setUsuario(usuarioEscolhido);
            emprestimo.setLivro(livroEscolhido);
            emprestimo.setDataEmprestimo(dataEmprestimo);
            emprestimo.setStatus("Vigente");
            emprestimo.setDataDevolucao(dataEmprestimo.plusDays(30));

            emprestimoRepository.save(emprestimo);

            return emprestimo;

        }else {
            throw new RuntimeException("Livro ou usuario não disponível");
        }
    }

    public Emprestimo atualizarEmprestimo(Integer idEmprestimo){
        Optional<Emprestimo> emprestimoAtualizar = emprestimoRepository.findById(idEmprestimo);

        if(emprestimoAtualizar.isEmpty()){
            throw new EmprestimoNotFoundException("Emprestimo não encontrado.");
        }else{
            Emprestimo emprestimoAlterado = emprestimoAtualizar.get();

            emprestimoAlterado.setDataDevolucao(LocalDate.now());
            emprestimoAlterado.setStatus("Concluído");

            emprestimoRepository.save(emprestimoAlterado);

            return emprestimoAlterado;
        }
    }
}