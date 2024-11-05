package br.fametro.controle_parental.dtos;

import br.fametro.controle_parental.entities.UserFilho;

public record ResponseUserFilhoDTO(
        Long id,
        String nome,
        String email,
        Long idResponsavel
) {
    public ResponseUserFilhoDTO(UserFilho data) {
        this(
                data.getId(),
                data.getNome(),
                data.getEmail(),
                data.getUserResponsavel().getId()
        );
    }
}
