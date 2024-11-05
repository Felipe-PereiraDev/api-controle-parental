package br.fametro.controle_parental.controller;

import br.fametro.controle_parental.dtos.CreateUrlVisitadaDTO;
import br.fametro.controle_parental.dtos.ResponseUrlVisitadaDTO;
import br.fametro.controle_parental.services.TokenService;
import br.fametro.controle_parental.services.UrlVisitidadasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user/f")
public class UrlVisitadasController {
    @Autowired
    private UrlVisitidadasService urlVisitadasService;
    @Autowired
    private TokenService tokenService;

    // Listar urls visitadas
    // (ok)
    @GetMapping("{idFilho}/historico-sites/{data}")
    public ResponseEntity<List<ResponseUrlVisitadaDTO>> listarUrls(@PathVariable("idFilho") Long idFilho,
                                                                   @PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                                                   @RequestHeader("Authorization") String authHeader) {
        long idResp = tokenService.getIdRespToken(authHeader);
        System.out.println(data);
        var lista_urls = urlVisitadasService.listarUrls(idResp, idFilho, data)
                .stream().map(ResponseUrlVisitadaDTO::new).toList();
        System.out.println("endpoint historico sites");
        return ResponseEntity.ok(lista_urls);
    }

    @GetMapping("/url-visitadas")
    public ResponseEntity<List<ResponseUrlVisitadaDTO>> getUrls(@RequestHeader("Authorization") String authHeader) {
        var idFilho = tokenService.getIdFilhoToken(authHeader);
        var lista_urls = urlVisitadasService.getUrls(idFilho)
                .stream().map(ResponseUrlVisitadaDTO::new).toList();
        return ResponseEntity.ok(lista_urls);
    }

    //(ok)
    // adiciona um novo site a lista de históricos
    @PostMapping("/historico-sites")
    public ResponseEntity<?> adicionarUrls(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody @Validated List<CreateUrlVisitadaDTO> data) {
        var idUserFilho = tokenService.getIdFilhoToken(authHeader);
        urlVisitadasService.salvaUrl(data, idUserFilho);
        return ResponseEntity.ok().build();
    }


}
