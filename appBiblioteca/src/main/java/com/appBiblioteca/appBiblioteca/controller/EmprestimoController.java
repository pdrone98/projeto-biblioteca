package com.appBiblioteca.appBiblioteca.controller;

import com.appBiblioteca.appBiblioteca.exceptions.EmprestimoNotFoundException;
import com.appBiblioteca.appBiblioteca.model.Emprestimo;
import com.appBiblioteca.appBiblioteca.model.Livro;
import com.appBiblioteca.appBiblioteca.model.Usuario;
import com.appBiblioteca.appBiblioteca.service.EmprestimoService;
import com.appBiblioteca.appBiblioteca.service.LivroService;
import com.appBiblioteca.appBiblioteca.service.UsuarioService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Emprestimo> buscarEmprestimoPorId(@PathVariable int id){

        Optional<Emprestimo> emprestimo = emprestimoService.buscarEmprestimoPorId(id);

        return emprestimo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Emprestimo> criarEmprestimo(@RequestParam Integer idUsuario, @RequestParam Integer idLivro, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataEmprestimo){
        Emprestimo emprestimo = emprestimoService.criarEmprestimo(idUsuario,idLivro, dataEmprestimo);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimo);
    }

    @PostMapping("/devolver/{idEmprestimo}")
    public Emprestimo devolverLivro(@PathVariable Integer idEmprestimo){
        Emprestimo emprestimo = emprestimoService.devolverLivro(idEmprestimo);
        return ResponseEntity.ok(emprestimo).getBody();
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<Emprestimo> atualizarEmprestimo(@PathVariable Integer id, @RequestBody Emprestimo emprestimoAtualizado){
        return emprestimoService.buscarEmprestimoPorId(id).map( emprestimo -> {
            emprestimo.setIdUsuario(emprestimoAtualizado.getIdUsuario());
            emprestimo.setIdLivro(emprestimoAtualizado.getIdLivro());
            emprestimo.setDataDevolucao(emprestimoAtualizado.getDataDevolucao());
            emprestimo.setStatus(emprestimoAtualizado.getStatus());

            Emprestimo emprestimoAlterado = emprestimoService.criarEmprestimo(emprestimo.getIdUsuario(), emprestimo.getIdLivro(), emprestimo.getDataEmprestimo());

            return ResponseEntity.ok(emprestimoAlterado);

        }).orElseThrow(()-> new EmprestimoNotFoundException("Emprestimo não encontrado."));
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
