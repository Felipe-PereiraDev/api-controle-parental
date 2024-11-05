package br.fametro.controle_parental.services;

import br.fametro.controle_parental.dtos.CreateUrlBloqueadaDTO;
import br.fametro.controle_parental.dtos.RequestBloquearUrl;
import br.fametro.controle_parental.dtos.ResponseUrlBloqueada;
import br.fametro.controle_parental.entities.UrlBloqueada;
import br.fametro.controle_parental.repositories.UrlBloqueadaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UrlBloqueadaService {
    @Autowired
    private  UrlBloqueadaRepository urlBloqueadaRepository;
    @Autowired
    private UserFilhoService userFilhoService;

    public void salvar(CreateUrlBloqueadaDTO data, Long idFilho) {
        var conta_filho = userFilhoService.findById(idFilho);
        if(!conta_filho.existsUrl(data.url())) {
            UrlBloqueada nova_url = new UrlBloqueada(data.url(), conta_filho);
            urlBloqueadaRepository.save(nova_url);
        }
    }

    public void mudarEstadoDesbloquearParaTrue(RequestBloquearUrl data, Long idFilho, Long idUserResp) {
        var user_filho = userFilhoService.isResponsavel(idFilho, idUserResp);
        var url = urlBloqueadaRepository.findByUserFilhoAndUrl(user_filho, data.url()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        url.setDesbloquear(true);
        urlBloqueadaRepository.save(url);
    }

    public List<ResponseUrlBloqueada> listarUrlsParaDesbloquear(Long idFilho) {
        var conta_filho = userFilhoService.findById(idFilho);
        var lista = urlBloqueadaRepository.findByUserFilhoAndDesbloquearTrue(conta_filho);
        return lista.stream().map(ResponseUrlBloqueada::new).toList();
    }

    @Transactional  
    public void deleteByUserFilhoAndUrl(Long idUserFilho, String url) {
        var userFilho =  userFilhoService.findById(idUserFilho);
        try {
            urlBloqueadaRepository.deleteByUserFilhoAndUrl(userFilho, url);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
