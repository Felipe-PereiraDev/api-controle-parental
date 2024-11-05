package br.fametro.controle_parental.dtos;

import br.fametro.controle_parental.entities.BloquearUrl;

public record ResponseBloquearUrl (
        Long id,
        String url,
        boolean bloqueado
){
    public ResponseBloquearUrl(BloquearUrl data) {
        this(
                data.getId(),
                data.getUrl(),
                data.isBloqueado()
        );
    }
}
