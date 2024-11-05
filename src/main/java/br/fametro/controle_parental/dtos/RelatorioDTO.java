package br.fametro.controle_parental.dtos;

import br.fametro.controle_parental.entities.Relatorio;
import br.fametro.controle_parental.entities.UrlBloqueada;

import java.util.List;
import java.util.Map;

public record RelatorioDTO(
        Map<String, Long> appTempoDeUso,
        List<String> sitesBloqueados
) {
    public RelatorioDTO(Relatorio relatorio) {
        this(relatorio.getDuracaoPorApp(), relatorio.getSitesBloqueados());
        System.out.println(relatorio.getDuracaoPorApp());
    }
}
