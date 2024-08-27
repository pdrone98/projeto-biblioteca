package com.appBiblioteca.appBiblioteca.controller;

import com.appBiblioteca.appBiblioteca.exceptions.EmprestimoNotFoundException;
import com.appBiblioteca.appBiblioteca.model.Emprestimo;
import com.appBiblioteca.appBiblioteca.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/biblioteca/emprestimo")
public class EmprestimoController {

    @Autowired
    public EmprestimoService emprestimoService;

    @GetMapping("/buscar")
    public List<Emprestimo> buscarTodosEmprestimos(){
        if(emprestimoService.buscarTodosEmprestimos().isEmpty()){
            throw new EmprestimoNotFoundException("Nenhum emprestimo foi realizado");
        }else {
            return emprestimoService.buscarTodosEmprestimos();
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Emprestimo> buscarEmprestimoPorId(@PathVariable Integer id){
        Optional<Emprestimo> emprestimo = emprestimoService.buscarEmprestimoPorId(id);
        return emprestimo.map(ResponseEntity::ok).orElseThrow(() ->  new EmprestimoNotFoundException("Emprestimo não encontrado"));
    }

    @GetMapping("/buscar/usuario/{idUsuario}")
    public ResponseEntity<List<Emprestimo>> getEmprestimosUsuario (@PathVariable Integer idUsuario) {
        List<Emprestimo> emprestimo = emprestimoService.buscarEmprestimosUsuarioId(idUsuario);
        if(emprestimo.isEmpty()){
            throw new EmprestimoNotFoundException("Emprestimo nao encontrado");
        }else{
            return ResponseEntity.ok(emprestimo);
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Emprestimo> criarEmprestimo(@RequestParam Integer idUsuario, @RequestParam Integer idLivro, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataEmprestimo){
        Emprestimo emprestimo = emprestimoService.criarEmprestimo(idUsuario,idLivro, dataEmprestimo);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimo);
    }

    @PutMapping("/alterar/{idEmprestimo}")
    public ResponseEntity<Emprestimo> atualizarEmprestimo(@PathVariable Integer idEmprestimo){
        Emprestimo emprestimo = emprestimoService.atualizarEmprestimo(idEmprestimo);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(emprestimo);
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
