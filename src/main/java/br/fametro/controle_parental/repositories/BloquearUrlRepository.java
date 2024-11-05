package br.fametro.controle_parental.repositories;

import br.fametro.controle_parental.entities.BloquearUrl;
import br.fametro.controle_parental.entities.UserFilho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloquearUrlRepository extends JpaRepository<BloquearUrl, Long> {
    @Query("SELECT b FROM BloquearUrl b WHERE b.userFilho.id = :idFilho AND b.userResponsavel.id = :idResp")
    List<BloquearUrl> findByUserFilhoAndUserResp(@Param("idFilho") Long idFilho, @Param("idResp") Long idResp);
    void deleteAllByBloqueadoTrue();
    BloquearUrl findByUserFilhoAndUrl(UserFilho userFilho, String url);
}
