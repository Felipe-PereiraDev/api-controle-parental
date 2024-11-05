package br.fametro.controle_parental.dtos;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateUrlVisitadaDTO (
        @NotBlank
        String url,
        @NotBlank
        LocalDateTime dataVisitada,
        String conteudo
){
}
