package br.fametro.controle_parental.dtos;

import jakarta.validation.constraints.NotBlank;

public record AddFilhoDTO(
        @NotBlank
        String email
) {
}
