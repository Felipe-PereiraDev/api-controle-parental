package br.fametro.controle_parental.dtos;

import br.fametro.controle_parental.entities.UrlBloqueada;

public record ResponseUrlBloqueada(
        String url
) {
    public ResponseUrlBloqueada(UrlBloqueada data) {
        this(
                data.getUrl()
        );
    }
}
