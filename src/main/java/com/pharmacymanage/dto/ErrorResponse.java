package com.pharmacymanage.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {

    private String titulo;

    private String mensagem;

    private Map<String, String> detalhes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;


    public ErrorResponse(String titulo, String mensagem, Map<String,String> detalhes) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.detalhes = detalhes;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String title, String message) {
        this(title, message, null);
    }

    public ErrorResponse(String title) {
        this(title, null);
    }

}