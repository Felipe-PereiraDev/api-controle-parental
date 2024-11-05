package br.fametro.controle_parental.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record RequestBloquearUrl(
        @NotBlank
        String url
) {
}
