package br.fametro.controle_parental.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Relatorio {
    private long totalEventos;
    private long duracaoTotal;
    private Map<String, Long> duracaoPorApp;
    private List<String> sitesBloqueados;

    public Relatorio(Map<String, Long> duracaoPorApp, List<String> sitesBloqueados) {
        this.duracaoPorApp = duracaoPorApp;
        this.sitesBloqueados = sitesBloqueados;

    }
}
