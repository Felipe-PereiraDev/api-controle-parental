package br.fametro.controle_parental.dtos;

import java.util.Map;

public record UsoMensalDTO(
        String tempoUsoApp,
        String mediaDiaria,
//        String tempoUsoSites,
        Map<String, String> appsESites
) {
}
