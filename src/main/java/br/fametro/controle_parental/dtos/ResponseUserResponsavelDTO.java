package br.fametro.controle_parental.dtos;

import br.fametro.controle_parental.entities.UserResponsavel;
import br.fametro.controle_parental.entities.enums.TipoSituacaoUsuario;

public record ResponseUserResponsavelDTO (
        Long id,
        String nome,
        String email,
        TipoSituacaoUsuario situacao
){
    public ResponseUserResponsavelDTO (UserResponsavel data) {
        this(
                data.getId(),
                data.getNome(),
                data.getEmail(),
                data.getSituacao()
        );
    }
}
