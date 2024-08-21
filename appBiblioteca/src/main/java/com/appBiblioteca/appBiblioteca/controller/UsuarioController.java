package com.appBiblioteca.appBiblioteca.controller;

import com.appBiblioteca.appBiblioteca.exceptions.UsuarioNotFoundException;
import com.appBiblioteca.appBiblioteca.model.Usuario;
import com.appBiblioteca.appBiblioteca.service.UsuarioService;
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
@RequestMapping("/biblioteca/usuario")
public class UsuarioController {

    @Autowired
    public UsuarioService usuarioService;

    @GetMapping("/buscar")
    public List<Usuario> buscarTodos() {
        if(usuarioService.buscarTodosUsuarios().isEmpty()){
            throw new UsuarioNotFoundException("Nenhum usuario cadastrado");
        }else {
            return usuarioService.buscarTodosUsuarios();
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId (@PathVariable int id){
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok).orElseThrow(() -> new UsuarioNotFoundException("Usuario nao encontrado"));
    }

    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario criarUsuario(@Valid @RequestBody Usuario usuario){
        return usuarioService.criarUsuario(usuario);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable int id){
        if(!usuarioService.buscarUsuarioPorId(id).isPresent()){
            throw new UsuarioNotFoundException("Usuario nao encontrado.");
        }else{
            usuarioService.deletarUsuario(id);
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<Usuario> alterarUsuario(@PathVariable int id, @Valid @RequestBody Usuario usuarioAtualizado) {
        return usuarioService.buscarUsuarioPorId(id).map( usuario -> {
            usuario.setDataCadastro(usuarioAtualizado.getDataCadastro());
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setTelefone(usuarioAtualizado.getTelefone());
            usuario.setEmail(usuarioAtualizado.getEmail());

            Usuario usuarioAlterado = usuarioService.criarUsuario(usuario);
            return ResponseEntity.ok(usuarioAlterado);
        }).orElseThrow(() -> new UsuarioNotFoundException("Usuario não encontrado."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> erros = new HashMap<>();
        for(FieldError error : exception.getBindingResult().getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(erros);
    }
}
