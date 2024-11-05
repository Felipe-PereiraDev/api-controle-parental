package br.fametro.controle_parental.dtos;

import br.fametro.controle_parental.entities.UrlVisitada;

public record ResponseUrlVisitadaDTO(
        String url,
        String dataVisitada,
        String conteudo
) {
    public ResponseUrlVisitadaDTO(UrlVisitada data) {
        this(
                data.getUrl(),
                data.getDataVisita().toString(),
                data.getConteudo()
        );
    }
}
