package com.appBiblioteca.appBiblioteca.repository;

import com.appBiblioteca.appBiblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {
    List<Emprestimo> findByUsuarioId(Integer idUsuario);
}
