package br.fametro.controle_parental.repositories;

import br.fametro.controle_parental.entities.UrlBloqueada;
import br.fametro.controle_parental.entities.UserFilho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlBloqueadaRepository extends JpaRepository<UrlBloqueada, Long> {
    Optional<UrlBloqueada> findByUserFilhoAndUrl(UserFilho userFilho, String url);
    List<UrlBloqueada> findByUserFilhoAndDesbloquearTrue(UserFilho userFilho);
    void deleteByUserFilhoAndUrl(UserFilho userFilho, String url);
}
