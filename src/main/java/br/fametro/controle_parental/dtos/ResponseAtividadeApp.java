package br.fametro.controle_parental.dtos;

import br.fametro.controle_parental.entities.AtividadeApp;

public record ResponseAtividadeApp (
        Long id,
        String nome,
        String hora_inicio
){
    public ResponseAtividadeApp(AtividadeApp data) {
        this(
                data.getId(),
                data.getNome(),
                data.getHora_inicio().toString()
        );
    }
}
