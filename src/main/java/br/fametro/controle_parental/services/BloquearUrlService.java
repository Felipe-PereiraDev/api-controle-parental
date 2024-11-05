package br.fametro.controle_parental.services;

import br.fametro.controle_parental.dtos.AtualizarBlockUrlDTO;
import br.fametro.controle_parental.dtos.CreateUrlBloqueadaDTO;
import br.fametro.controle_parental.entities.BloquearUrl;
import br.fametro.controle_parental.repositories.BloquearUrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloquearUrlService {
    @Autowired
    private BloquearUrlRepository bloquearUrlRepository;
    @Autowired
    private UserFilhoService filhoService;
    @Autowired
    private UserResponsavelService responsavelService;
    @Autowired
    private UrlBloqueadaService urlBloqueadaService;

    public void salvarUrl(String url, Long idResp, Long idFilho) {
        var conta_filho = filhoService.isResponsavel(idFilho, idResp);
        var conta_resp = conta_filho.getUserResponsavel();
        BloquearUrl blockUrl = new BloquearUrl(url, conta_resp, conta_filho);
        if (conta_resp.getBloquearUrl().stream().noneMatch(u -> u.getUrl().equals(url))) {
            bloquearUrlRepository.save(blockUrl);
        }
    }

    public List<BloquearUrl> listarUrlsNaoBloqueadas(Long idFilho, Long idResp) {
        filhoService.isResponsavel(idFilho, idResp);
        return bloquearUrlRepository.findByUserFilhoAndUserResp(idFilho, idResp)
                .stream()
                .filter(url -> !url.isBloqueado())
                .toList();
    }

    public List<BloquearUrl> listarUrlsBloqueadas(Long idFilho, Long idResp) {
        filhoService.isResponsavel(idFilho, idResp);
        return bloquearUrlRepository.findByUserFilhoAndUserResp(idFilho, idResp)
                .stream()
                .filter(BloquearUrl::isBloqueado)
                .toList();
    }

    @Transactional
    public void atualizar(Long idResp, Long idFilho, List<AtualizarBlockUrlDTO> data) {
        var userFilho = filhoService.isResponsavel(idFilho, idResp);
        for (AtualizarBlockUrlDTO urlDTO : data) {
            var urlAtt = bloquearUrlRepository.findByUserFilhoAndUrl(userFilho, urlDTO.url());
            urlAtt.mudarEstadoParaTrue();

            // Salva as URLs bloqueadas na lista do responsável
            var listaUrlsBloqueadasResp = listarUrlsBloqueadas(idFilho, idResp).stream()
                    .map(CreateUrlBloqueadaDTO::new)
                    .toList();

            listaUrlsBloqueadasResp.forEach(url -> urlBloqueadaService.salvar(url, idFilho));
        }

        // Remove as URLs que estão bloqueadas
        bloquearUrlRepository.deleteAllByBloqueadoTrue();
    }
}

