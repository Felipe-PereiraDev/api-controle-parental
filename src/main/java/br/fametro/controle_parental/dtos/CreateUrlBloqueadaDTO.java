package br.fametro.controle_parental.dtos;

import br.fametro.controle_parental.entities.BloquearUrl;
import jakarta.validation.constraints.NotBlank;

public record CreateUrlBloqueadaDTO(
        @NotBlank
        String url
){
        public CreateUrlBloqueadaDTO(BloquearUrl data){
                this(
                        data.getUrl()
                );
        }
}
