package com.appBiblioteca.appBiblioteca.exceptions;

public class EmprestimoNotFoundException extends RuntimeException {

    public EmprestimoNotFoundException (String mensagem){
        super(mensagem);
    }
}
