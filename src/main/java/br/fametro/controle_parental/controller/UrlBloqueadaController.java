package br.fametro.controle_parental.controller;


import br.fametro.controle_parental.dtos.AtualizarBlockUrlDTO;
import br.fametro.controle_parental.dtos.ResponseBloquearUrl;
import br.fametro.controle_parental.services.BloquearUrlService;
import br.fametro.controle_parental.services.TokenService;
import br.fametro.controle_parental.services.UrlBloqueadaService;
import br.fametro.controle_parental.services.UserFilhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/f")
public class UrlBloqueadaController {
    @Autowired
    private UrlBloqueadaService urlBloqueadaService;
    @Autowired
    private BloquearUrlService bloquearUrlService;
    @Autowired
    private TokenService tokenService;

    // Função que lista todas as urls que ainda não foram bloqueadas
    // no app do filho devesse passa
    @GetMapping("/{idResp}/bloquear-url")
    public ResponseEntity<List<ResponseBloquearUrl>> listarUrlsNaoBloqueadas(@PathVariable("idResp") Long idResp,
                                                                             @RequestHeader("Authorization") String authHeader){
        var idFilho = tokenService.getIdFilhoToken(authHeader);
        var lista = bloquearUrlService.listarUrlsNaoBloqueadas(idFilho, idResp)
                .stream().map(ResponseBloquearUrl::new).toList();
        return ResponseEntity.ok(lista);
    }

    // endpoint pra atualizar o status da url bloqueada para true
    @PutMapping("/{idResp}/bloquear-url")
    public ResponseEntity<?> atualizarBlockUrl(@PathVariable("idResp") Long idResp,
                                               @RequestBody @Validated List<AtualizarBlockUrlDTO> data,
                                               @RequestHeader("Authorization") String authHeader) {
        var idFilho = tokenService.getIdFilhoToken(authHeader);
        bloquearUrlService.atualizar(idResp, idFilho, data);
        return ResponseEntity.noContent().build();
    }
}
