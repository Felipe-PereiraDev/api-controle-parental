package br.fametro.controle_parental.controller;


import br.fametro.controle_parental.dtos.AddFilhoDTO;
import br.fametro.controle_parental.dtos.RequestBloquearUrl;
import br.fametro.controle_parental.entities.UserResponsavel;
import br.fametro.controle_parental.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/r")
public class UserResponsavelController {
    @Autowired
    private UserResponsavelService responsavelService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BloquearUrlService bloquearUrlService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/listar")
    public ResponseEntity<List<UserResponsavel>> listar() {
        return ResponseEntity.ok(responsavelService.listar());
    }

    // (ok)
    // função para adicionar filho
    @PostMapping("/add-filho")
    public ResponseEntity<?> addFilho(@RequestBody @Validated AddFilhoDTO data,
                                      @RequestHeader("Authorization") String authHeader) {
        var idResp = tokenService.getIdRespToken(authHeader);
        responsavelService.addFilho(data.email(), idResp);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idFilho}/bloquear-url")
    public ResponseEntity<?> bloquearUrl(@RequestBody @Validated RequestBloquearUrl data,
                                         @RequestHeader("Authorization") String authHeader,
                                         @PathVariable("idFilho") Long idFilho) {
        var idResp = tokenService.getIdRespToken(authHeader);
        bloquearUrlService.salvarUrl(data.url(), idResp, idFilho);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filhos")
    public ResponseEntity<?> listarFilhos(@RequestHeader("Authorization") String authHeader) {
        var id = tokenService.getIdRespToken(authHeader);
        System.out.println("função listar filhos foi chamada");
        return ResponseEntity.ok(responsavelService.listarFilhos(id));
    }

    // Gerar Relatórios
    // (ok)
    @PostMapping("{idFilho}/relatorio-semanal")
    public ResponseEntity<?> gerarRelatorioSemanal(@PathVariable("idFilho") Long idFilho,
                                                   @RequestHeader("Authorization") String authHeader) {
        var idResp = tokenService.getIdRespToken(authHeader);
        relatorioService.gerarRelatorioSemanal(idFilho, idResp);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{idFilho}/relatorio-mensal")
    public ResponseEntity<?> gerarRelatorioMensal(@PathVariable("idFilho") Long idFilho,
                                                  @RequestHeader("Authorization") String authHeader,
                                                  @RequestParam(value = "mes", required = false) Integer mes) {
        var idResp = tokenService.getIdRespToken(authHeader);
        relatorioService.gerarRelatorioMensal(idFilho, idResp, mes);
        return ResponseEntity.ok().build();
    }
 }
