package br.fametro.controle_parental.services;

import br.fametro.controle_parental.entities.UsuarioVerificador;
import br.fametro.controle_parental.entities.enums.TipoSituacaoUsuario;
import br.fametro.controle_parental.repositories.UserFilhoRepository;
import br.fametro.controle_parental.repositories.UserResponsavelRepository;
import br.fametro.controle_parental.repositories.UsuarioVerificadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioVerificadorService {
    @Autowired
    private UsuarioVerificadorRepository usuarioVerificadorRepository;

    @Autowired
    private UserResponsavelRepository userResponsavelRepository;

    @Autowired
    private UserFilhoRepository userFilhoRepository;

    public boolean verifyUserByToken(String token) {
        Optional<UsuarioVerificador> usuarioVerificador = usuarioVerificadorRepository.findByUuid(UUID.fromString(token));

        if (usuarioVerificador.isPresent()) {
            UsuarioVerificador verificador = usuarioVerificador.get();

            // Verifica se o token ainda está válido
            if (verificador.getDataExpiracao().isAfter(Instant.now())) {
                // Ativa o usuário responsável
                var userResponsavel = verificador.getUserResponsavel();
                userResponsavel.setSituacao(TipoSituacaoUsuario.ATIVO);
                userResponsavelRepository.save(userResponsavel);

                // Remove o token de verificação
                usuarioVerificadorRepository.delete(verificador);

                return true;
            }
        }

        return false;
    }


    // verificar a conta do filho
    public boolean verifyUserByTokenFilho(String token) {
        Optional<UsuarioVerificador> usuarioVerificador = usuarioVerificadorRepository.findByUuid(UUID.fromString(token));

        if (usuarioVerificador.isPresent()) {
            UsuarioVerificador verificador = usuarioVerificador.get();

            // Verifica se o token ainda está válido
            if (verificador.getDataExpiracao().isAfter(Instant.now())) {
                // Ativa o usuário responsável
                var userFilho = verificador.getUserFilho();
                userFilho.setSituacao(TipoSituacaoUsuario.ATIVO);
                userFilhoRepository.save(userFilho);
                // Remove o token de verificação
                usuarioVerificadorRepository.delete(verificador);

                return true;
            }
        }

        return false;
    }

    public boolean verifyUserByTokenJuntarFamilia(String token) {
        Optional<UsuarioVerificador> usuarioVerificador = usuarioVerificadorRepository.findByUuid(UUID.fromString(token));

        if (usuarioVerificador.isPresent()) {
            UsuarioVerificador verificador = usuarioVerificador.get();

            // Verifica se o token ainda está válido
            if (verificador.getDataExpiracao().isAfter(Instant.now())) {
                // Ativa o usuário responsável
                var userFilho = verificador.getUserFilho();
                var userResponsavel = verificador.getUserResponsavel();
                userFilho.setUserResponsavel(userResponsavel);
                userFilhoRepository.save(userFilho);
                usuarioVerificadorRepository.delete(verificador);
                return true;
            }
        }

        return false;
    }
}
