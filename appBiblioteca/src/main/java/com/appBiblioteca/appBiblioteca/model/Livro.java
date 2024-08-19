package com.appBiblioteca.appBiblioteca.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "O campo titulo não pode ser nulo.")
    private String titulo;
    @NotNull(message = "O campo autor não pode ser nulo.")
    private String autor;
    @NotNull(message = "O campo isbn não pode ser nulo.")
    private String isbn;
    @NotNull(message = "O campo categoria não pode ser nulo.")
    private String categoria;
    @NotNull(message = "O campo dataPublicacao não pode ser nulo.")
    private LocalDate dataPublicacao;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprestimo> listaLivrosEmprestados = new ArrayList<>();

    public List<Emprestimo> getListaLivrosEmprestados() {
        return listaLivrosEmprestados;
    }

    public void setListaLivrosEmprestados(List<Emprestimo> listaLivrosEmprestados) {
        this.listaLivrosEmprestados = listaLivrosEmprestados;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
}
