package br.fametro.controle_parental.repositories;

import br.fametro.controle_parental.entities.UsuarioVerificador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioVerificadorRepository extends JpaRepository <UsuarioVerificador, Long> {
    Optional<UsuarioVerificador> findByUuid(UUID token);

}
