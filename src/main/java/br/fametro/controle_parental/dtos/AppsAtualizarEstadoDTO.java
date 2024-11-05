package br.fametro.controle_parental.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppsAtualizarEstadoDTO(
        @NotNull
        String nome,
        @NotNull
        boolean ativo
) {
}
