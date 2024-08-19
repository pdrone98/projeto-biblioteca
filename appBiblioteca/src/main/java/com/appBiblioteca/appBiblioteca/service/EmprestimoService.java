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

    public Optional<Emprestimo> buscarEmprestimoPorId(int id) {
        return emprestimoRepository.findById(id);
    }

    public Emprestimo devolverLivro(Integer idEmprestimo){
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo).orElseThrow(() -> new EmprestimoNotFoundException("Emprestimo não encontrado."));

        if(emprestimo.getStatus().equals("Emprestado")){

            emprestimo.setStatus("Devolvido");
            emprestimo.setDataDevolucao(LocalDate.now());

            return emprestimoRepository.save(emprestimo);
        }else {
            throw new EmprestimoNotFoundException("O emprestimo já foi devolvido");
        }
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

            boolean existeEmprestimoAtivo = livroEscolhido.getListaLivrosEmprestados().stream().anyMatch(e -> e.getStatus().equals("Emprestado"));

            if(existeEmprestimoAtivo){
                throw new LivroNotFoundException("O livro escolhido já está emprestado, escolha outro livro.");
            }

            //cria emprestimo
            Emprestimo emprestimo = new Emprestimo();

            emprestimo.setUsuario(usuarioEscolhido);
            emprestimo.setIdUsuario(idUsuario);
            emprestimo.setLivro(livroEscolhido);
            emprestimo.setIdLivro(idLivro);
            emprestimo.setDataEmprestimo(dataEmprestimo);
            emprestimo.setStatus("Emprestado");
            emprestimo.setDataDevolucao(dataEmprestimo.plusDays(30));

            livroEscolhido.getListaLivrosEmprestados().add(emprestimo);
            usuarioEscolhido.getListaEmprestimos().add(emprestimo);

            emprestimoRepository.save(emprestimo);

            return emprestimo;

        }else {
            throw new RuntimeException("Livro ou usuario não disponível");
        }
    }
}