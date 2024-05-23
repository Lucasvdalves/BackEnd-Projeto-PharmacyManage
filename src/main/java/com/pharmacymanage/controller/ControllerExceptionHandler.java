package com.pharmacymanage.controller;

import com.pharmacymanage.dto.ErrorResponse;
import com.pharmacymanage.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<Object> handleRegistroNaoEncontradoException(RegistroNaoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse("Registro Inexistente", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RegistroJaCadastradoException.class)
    public ResponseEntity<Object> handleResourceAlreadyRegistered(RegistroJaCadastradoException ex) {
        ErrorResponse error = new ErrorResponse("Registro Existente", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(QuantidadeInvalidaException.class)
    public ResponseEntity<Object> handleQuantidadeInvalida(QuantidadeInvalidaException ex) {
        ErrorResponse error = new ErrorResponse("Quantidade Inválida", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CepNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleCepNaoEncontradoException(CepNaoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse("CEP Não Encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse error = new ErrorResponse("Dados Inválidos", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ErrorResponse error = new ErrorResponse("Dados Inválidos", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NenhumRegistroEncontradoException.class)
    public ResponseEntity<Object> handleNenhumRegistroEncontradoException(NenhumRegistroEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<Object> handleRequisicaoInvalidaException(RequisicaoInvalidaException ex) {
        ErrorResponse error = new ErrorResponse("Requisição Inválida", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CnpjInvalidoException.class)
    public ResponseEntity<Object> handleCnpjInvalidoException(CnpjInvalidoException ex) {
        ErrorResponse error = new ErrorResponse("CNPJ inválido.", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}