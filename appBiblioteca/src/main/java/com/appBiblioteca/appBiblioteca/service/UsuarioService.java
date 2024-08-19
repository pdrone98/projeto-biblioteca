package com.appBiblioteca.appBiblioteca.service;


import com.appBiblioteca.appBiblioteca.exceptions.UsuarioNotFoundException;
import com.appBiblioteca.appBiblioteca.model.Usuario;
import com.appBiblioteca.appBiblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.thymeleaf.engine.IThrottledTemplateWriterControl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> buscarTodosUsuarios(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(int id){
        return usuarioRepository.findById(id);
    }

    public Usuario criarUsuario(Usuario usuario){
        if(usuario.getDataCadastro().isAfter(LocalDate.now())){
            throw new RuntimeException("Data de cadastro não pode ser maior que a data atual.");
        }
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(int id){
        usuarioRepository.deleteById(id);
    }
}
