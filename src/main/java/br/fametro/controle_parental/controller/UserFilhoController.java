package br.fametro.controle_parental.controller;

import br.fametro.controle_parental.dtos.*;
import br.fametro.controle_parental.entities.Relatorio;
import br.fametro.controle_parental.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/f")
public class UserFilhoController {
    @Autowired
    private UserFilhoService userFilhoService;
    @Autowired
    private UrlBloqueadaService urlBloqueadaService;
    @Autowired
    private AtividadeAppService atividadeAppService;
    @Autowired
    private RelatorioService relatorioService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/apps-abertos")
    public ResponseEntity<?> salvarAtividadeApp(@RequestHeader("Authorization") String authHeader,
                                                @RequestBody @Validated RequestAtividadeApp data) {
        var idFilho = tokenService.getIdFilhoToken(authHeader);
        atividadeAppService.salvar(data, idFilho);
        return ResponseEntity.ok().build();
    }

    // Listar as atividades dos apps
    @GetMapping("{idFilho}/apps-abertos")
    public ResponseEntity<List<ResponseAtividadeApp>> listarAtividadeApp(@PathVariable("idFilho") Long idFilho,
                                                                         @RequestHeader("Authorization") String authHeader) {
        var idResp = tokenService.getIdRespToken(authHeader);
        var lista = atividadeAppService.listarAppsAtivos(idResp, idFilho).stream().map(ResponseAtividadeApp::new).toList();
        return ResponseEntity.ok(lista);
    }

    // remover janelas fechadas do banco de dados
    // endPoint monitorador
    @PutMapping("/apps-atualizar")
    public ResponseEntity<?> atualizarStatus(@RequestBody @Validated AppsAtualizarEstadoDTO data,
                                             @RequestHeader("Authorization") String authHeader) {
        var idFilho = tokenService.getIdFilhoToken(authHeader);
        System.out.println(data);
        System.out.println("função apps-atualizar sendo chamada, dados do header :" + data);
        atividadeAppService.atualizarEstado(idFilho, data);
        return ResponseEntity.noContent().build();
    }   

    // salva url bloqueada
    @PostMapping("/url-bloqueada")
    @Transactional
    public ResponseEntity<?> salvarUrlBloqueada(@RequestBody @Validated CreateUrlBloqueadaDTO data,
                                                @RequestHeader("Authorization") String authHeader) {
        var idFilho = tokenService.getIdFilhoToken(authHeader);
        urlBloqueadaService.salvar(data, idFilho);
        return ResponseEntity.ok().build();
    }

    // Listar Todas as urls bloqueadas pelo Responsável
    // (ok)
    @GetMapping("/{idFilho}/url-bloqueada")
    public ResponseEntity<List<ResponseUrlBloqueada>> listarUrlsBloqueadas(@PathVariable("idFilho") Long idFilho,
                                                                           @RequestHeader("Authorization") String authHeader) {
        var idResp = tokenService.getIdRespToken(authHeader);
        var urls = userFilhoService.listaUrlsBloqueadas(idFilho, idResp).stream().map(ResponseUrlBloqueada::new).toList();
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/desbloquear-url")
    public ResponseEntity<List<ResponseUrlBloqueada>> listarUrlsParaDesbloquear(@RequestHeader("Authorization") String authHeader) {
        var idUserFilho = tokenService.getIdFilhoToken(authHeader);
        var lista = urlBloqueadaService.listarUrlsParaDesbloquear(idUserFilho);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("{idFilho}/desbloquear-url")
    public ResponseEntity<?> desbloquearUrl(@PathVariable("idFilho") Long idFilho,
                                                                     @RequestHeader("Authorization") String authHeader,
                                                                     @RequestBody @Validated RequestBloquearUrl data) {
        var idUserResp = tokenService.getIdRespToken(authHeader);
        urlBloqueadaService.mudarEstadoDesbloquearParaTrue(data, idFilho, idUserResp);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/desbloquear-url")
    public ResponseEntity<?> deletarUrlBloqueada(@RequestHeader("Authorization") String authHeader,
                                                                          @RequestBody AtualizarBlockUrlDTO data) {
        var idUserFilho = tokenService.getIdFilhoToken(authHeader);
        urlBloqueadaService.deleteByUserFilhoAndUrl(idUserFilho, data.url());
        return ResponseEntity.noContent().build();
    }
    //(ok)
    @GetMapping("{idFilho}/grafico-uso-mensal")
    public ResponseEntity<?> usoMensal(@PathVariable("idFilho") Long idFilho,
                                       @RequestHeader("Authorization") String authHeader) {
        var idResp = tokenService.getIdRespToken(authHeader);
        return ResponseEntity.ok(relatorioService.dadosUsoMensal(idFilho, idResp));
    }
}
