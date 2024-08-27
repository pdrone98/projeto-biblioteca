package com.appBiblioteca.appBiblioteca.repository;

import com.appBiblioteca.appBiblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {
    // Busca livros por categoria que ainda não foram emprestados pelo usuário
    @Query("SELECT l FROM Livro l WHERE l.categoria = :categoria AND l.id NOT IN (SELECT e.livro.id FROM Emprestimo e WHERE e.usuario.id = :idUsuario)")
    List<Livro> findLivrosPorCategoriaNaoEmprestadosPeloUsuario(@Param("categoria") String categoria, @Param("idUsuario") Integer idUsuario);

}
