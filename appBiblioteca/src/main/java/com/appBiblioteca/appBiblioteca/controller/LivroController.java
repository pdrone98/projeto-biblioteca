package com.appBiblioteca.appBiblioteca.controller;

import com.appBiblioteca.appBiblioteca.exceptions.LivroNotFoundException;
import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/biblioteca/livro")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping("/buscar")
    public List<Livro> buscarTodos() {
        if(livroService.buscarTodosLivros().isEmpty()){
            throw new LivroNotFoundException("Nenhum livro cadastrado.");
        }else{
            return livroService.buscarTodosLivros();
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Livro> buscarLivroPorId(@PathVariable int id) {
        Optional<Livro> livro = livroService.buscarLivroPorId(id);
        return livro.map(ResponseEntity::ok).orElseThrow(() -> new LivroNotFoundException("Livro não encontrado."));
    }

    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public Livro salvarLivro(@Valid @RequestBody Livro livro) {
        return livroService.salvarLivro(livro);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarLivroPorId(@PathVariable int id) {
        if (!livroService.buscarLivroPorId(id).isPresent()) {
            throw new LivroNotFoundException("Livro não encontrado");
        } else {
            livroService.deletarLivro(id);
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<Livro> alterarLivro(@PathVariable Integer id, @Valid @RequestBody Livro livroAtualizado) {
        return livroService.buscarLivroPorId(id).map( livro -> {
            livro.setAutor(livroAtualizado.getAutor());
            livro.setCategoria(livroAtualizado.getCategoria());
            livro.setTitulo(livroAtualizado.getTitulo());
            livro.setDataPublicacao(livroAtualizado.getDataPublicacao());
            livro.setIsbn(livroAtualizado.getIsbn());

            Livro livroAlterado = livroService.salvarLivro(livro);
            return ResponseEntity.ok(livroAlterado);
        }).orElseThrow(() -> new LivroNotFoundException("Livro não encontrado."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(erros);
    }

}
