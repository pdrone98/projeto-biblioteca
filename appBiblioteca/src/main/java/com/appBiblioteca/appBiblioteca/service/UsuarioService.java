package com.appBiblioteca.appBiblioteca.service;

import com.appBiblioteca.appBiblioteca.model.Usuario;
import com.appBiblioteca.appBiblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

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

    public Optional<Usuario> buscarUsuarioPorId(Integer id){
        return usuarioRepository.findById(id);
    }

    public Usuario criarUsuario(Usuario usuario){
        if(usuario.getDataCadastro().isAfter(LocalDate.now())){
            throw new RuntimeException("Data de cadastro não pode ser maior que a data atual.");
        }
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Integer id){
        usuarioRepository.deleteById(id);
    }
}
