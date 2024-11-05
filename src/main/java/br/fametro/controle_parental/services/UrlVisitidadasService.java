package br.fametro.controle_parental.services;

import br.fametro.controle_parental.dtos.CreateUrlVisitadaDTO;
import br.fametro.controle_parental.entities.UrlVisitada;
import br.fametro.controle_parental.entities.UserFilho;
import br.fametro.controle_parental.repositories.UrlVisitadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlVisitidadasService {
    @Autowired
    private UrlVisitadaRepository urlVisitadaRepository;
    @Autowired
    private UserFilhoService userFilhoService;
    @Autowired
    private UserResponsavelService userResponsavelService;


    public void salvaUrl(List<CreateUrlVisitadaDTO> data, Long id) {
        var conta_filho = userFilhoService.findById(id);
        var lista_url_exists = conta_filho.getUrlVisitadas();

        var urlsParaSalvar = data.stream()
                .filter(dto -> lista_url_exists.stream()
                        .noneMatch(urlVisitada -> urlVisitada.getUrl()
                                .equals(dto.url())))
                .map(dto -> new UrlVisitada(dto.url(), dto.dataVisitada(), dto.conteudo(), conta_filho))
                .toList();
        urlVisitadaRepository.saveAll(urlsParaSalvar);
    }

    public List<UrlVisitada> listarUrls(Long idResp, Long idFilho, LocalDate data) {
        var userFilho = userFilhoService.isResponsavel(idFilho, idResp);
        LocalDateTime inicio = data.atStartOfDay(); // 00:00:00 do dia
        LocalDateTime fim = data.plusDays(1).atStartOfDay(); // 00:00:00 do dia seguinte
        return urlVisitadaRepository.findByUserFilhoAndDataVisita(idFilho, inicio, fim);
    }

    public List<UrlVisitada> getUrls(Long idFilho) {
        var userFilho = userFilhoService.findById(idFilho);
        return listarUrls(userFilho.getUserResponsavel().getId(), idFilho, LocalDate.now());
    }
}
