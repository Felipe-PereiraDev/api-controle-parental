package br.fametro.controle_parental.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestAtividadeApp(
        @NotBlank
        String nome,
        @NotNull
        LocalDateTime hora_inicio,
        @NotNull
        boolean ativo
) {
}
