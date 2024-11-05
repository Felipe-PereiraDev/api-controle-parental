package br.fametro.controle_parental.services;

import br.fametro.controle_parental.dtos.RelatorioDTO;
import br.fametro.controle_parental.dtos.UsoMensalDTO;
import br.fametro.controle_parental.entities.Log;
import br.fametro.controle_parental.entities.Relatorio;
import br.fametro.controle_parental.entities.UserFilho;
import br.fametro.controle_parental.repositories.LogRepository;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserFilhoService userFilhoService;
    @Autowired
    private EmailService emailService;

    public void gerarRelatorioSemanal(Long idFilho,Long idResp) {
        var userFilho = userFilhoService.isResponsavel(idFilho, idResp);
        List<Log> logs = obterLogsSemanal(idFilho);
        var relatorio = processarRelatorio(logs, userFilho);
        var dto = new RelatorioDTO(relatorio);
        var emailResp = userFilho.getUserResponsavel().getEmail();
        gerarRelatorioPDF(dto.appTempoDeUso(), dto.sitesBloqueados(), emailResp, "Semanal", userFilho.getNome(),
                userFilho.getUserResponsavel().getNome(), null, null);
    }

    public void gerarRelatorioMensal(Long idFilho, Long idResp, Integer mes) {
        var userFilho = userFilhoService.isResponsavel(idFilho, idResp);
        List<Log> logs = obterLogsPorMes(idFilho, mes);
        var relatorio = processarRelatorio(logs, userFilho);
        var dto = new RelatorioDTO(relatorio);
        var emailResp = userFilho.getUserResponsavel().getEmail();
        String nomeFilho = userFilho.getNome();
        String nomeResp = userFilho.getUserResponsavel().getNome();

        gerarRelatorioPDF(dto.appTempoDeUso(), dto.sitesBloqueados(), emailResp, "Mensal", nomeFilho, nomeResp,
                mes, LocalDateTime.now().getYear());
    }


    private List<Log> obterLogsSemanal(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        return logRepository.findLogsByUserIdAndDateRange(userId, startDate, endDate);
    }

    private List<Log> obterLogsMesAtual(Long userId) {
        LocalDate hoje = LocalDate.now();
        LocalDate primeiroDiaMes = LocalDate.now().withDayOfMonth(1);
        return logRepository.findLogsByUserIdAndDateRange(userId, primeiroDiaMes, hoje);
    }

    private List<Log> obterLogsPorMes(Long userId, Integer mes) {
        LocalDate hoje = LocalDate.now();
        YearMonth anoMes = (mes == null) ? YearMonth.now() : YearMonth.of(hoje.getYear(), mes);

        LocalDate primeiroDiaMes = anoMes.atDay(1);
        LocalDate ultimoDiaMes;


        if (anoMes.equals(YearMonth.now())) {
            ultimoDiaMes = hoje;
        } else {
            ultimoDiaMes = anoMes.atEndOfMonth();
        }

        return logRepository.findLogsByUserIdAndDateRange(userId, primeiroDiaMes, ultimoDiaMes);
    }


    private Relatorio processarRelatorio(List<Log> logs, UserFilho userFilho) {
        Map<String, Long> duracaoPorApp = logs.stream()
                .collect(Collectors.groupingBy(Log::getDetalhes, Collectors.summingLong(Log::getDuracao)));
        var sitesBloqueados = userFilho.getSitesBloqueados();
        System.out.println("Sites Bloqueados:" + sitesBloqueados);
        return new Relatorio(duracaoPorApp, sitesBloqueados);
    }

    public void gerarRelatorioPDF(Map<String, Long> appTempoDeUso, List<String> sitesBloqueados,
                                  String emailDestinatario, String periodo, String nomeFilho, String nomeResp,
                                  Integer mesDesejado, Integer anoDesejado) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            LocalDate periodoInicio;
            LocalDate periodoFim = LocalDate.now();

            if (periodo.equalsIgnoreCase("Semanal")) {
                periodoInicio = periodoFim.minusDays(6);
            } else {
                if (mesDesejado != null && anoDesejado != null) {
                    periodoInicio = LocalDate.of(anoDesejado, mesDesejado, 1);
                    periodoFim = periodoInicio.withDayOfMonth(periodoInicio.lengthOfMonth());
                } else {
                    periodoInicio = periodoFim.withDayOfMonth(1);
                }
            }

            DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String dataInicioFormatada = periodoInicio.format(dataFormat);
            String dataFimFormatada = periodoFim.format(dataFormat);

            Text texto = new Text("Relatório de Uso " + periodo).setFontSize(24).setBold();
            Paragraph titulo = new Paragraph(texto).setTextAlignment(TextAlignment.CENTER).setMarginBottom(20);
            document.add(titulo);
            document.add(new Paragraph("Relatório solicitado por: " + nomeResp));
            document.add(new Paragraph("Nome do filho: " + nomeFilho));
            document.add(new Paragraph("Período: " + dataInicioFormatada + " até " + dataFimFormatada));

            document.add(new Paragraph("\nTempo de Uso dos Aplicativos"));
            Table appTable = new Table(2);
            Cell aplicativoCell = new Cell().add(new Paragraph(new Text("Aplicativo").setFontColor(ColorConstants.BLUE)));
            Cell tempoUsoCell = new Cell().add(new Paragraph(new Text("Tempo de Uso (horas/min)").setFontColor(ColorConstants.BLUE)));

            appTable.addCell(aplicativoCell);
            appTable.addCell(tempoUsoCell);

            List<Map.Entry<String, Long>> sortedEntries = appTempoDeUso.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .collect(Collectors.toList());

            long minutosTotal = 0;
            for (Map.Entry<String, Long> entry : sortedEntries) {
                appTable.addCell(new Cell().add(new Paragraph(entry.getKey())));
                long mn = entry.getValue();
                appTable.addCell(new Cell().add(new Paragraph(formatarHoras((int) (mn / 60), (int) (mn % 60)))));
                minutosTotal += mn;
            }

            long qtdeDias;
            if (mesDesejado != null && anoDesejado != null) {
                qtdeDias = periodoInicio.lengthOfMonth(); // Total de dias no mês especificado
            } else {
                qtdeDias = Math.max(1, periodoInicio.until(periodoFim.plusDays(1)).getDays()); // Dias entre periodoInicio e periodoFim
            }

            int mediaMinutos = (int) (minutosTotal / qtdeDias);
            String mediaDiaria = formatarHoras(mediaMinutos / 60, mediaMinutos % 60);

            document.add(appTable);

            int qtdeHoras = (int) (minutosTotal / 60);
            int qtdeMinutos = (int) (minutosTotal % 60);
            String tempo = formatarHoras(qtdeHoras, qtdeMinutos);
            document.add(new Paragraph("O total de uso de aplicativos foi de " + tempo));
            document.add(new Paragraph("A média de tempo diária foi de " + mediaDiaria));

            document.add(new Paragraph("\nSites Bloqueados:"));
            String sites = "[" + String.join(", ", sitesBloqueados) + "]";
            document.add(new Paragraph(sites));

            document.close();

            emailService.enviarEmailComAnexo(emailDestinatario, "Relatório", "Segue o anexo com o pdf", baos.toByteArray());
        } catch (Exception e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
        }
    }



    public UsoMensalDTO dadosUsoMensal (Long idFilho, Long idResp) {
        userFilhoService.isResponsavel(idFilho, idResp);
        List<Log> dadosMensal = obterLogsMesAtual(idFilho);
        long totalMinutos = 0L;
        LocalDate hoje = LocalDate.now();
        LocalDate primeiroDiaMes = hoje.withDayOfMonth(1);
        long totalDias = primeiroDiaMes.until(hoje).getDays();
        for (Log log : dadosMensal) {
            totalMinutos += log.getDuracao();
        }

        // calcular media diária
        int mediaMinutos = (int) (totalMinutos / totalDias);
        int horasMedia = mediaMinutos / 60;
        int minutosMedia = mediaMinutos % 60;
        int horasUsoApp = (int) (totalMinutos / 60);
        int minutosUsoApp = (int) (totalMinutos % 60);
        String mediaDiaria = formatarHoras(horasMedia, minutosMedia);
        String tempoUsoApp = formatarHoras(horasUsoApp, minutosUsoApp);
        var appsESites = getAppsETempo(dadosMensal);
        return new UsoMensalDTO(tempoUsoApp, mediaDiaria, appsESites);
    }

    private String formatarHoras(int horas, int minutos) {
        return horas > 0 ? horas + "h " + minutos + "min" : minutos + "min";
    }

    private Map<String, String> getAppsETempo(List<Log> logs) {
        Map<String, Long> tempoTotalPorApp = new HashMap<>();
        for (Log log : logs) {
            String nomeApp = log.getDetalhes();
            Long duracao = log.getDuracao();
            tempoTotalPorApp.put(nomeApp, tempoTotalPorApp.getOrDefault(nomeApp, 0L) + duracao);
        }
        List<Map.Entry<String, Long>> sortedEntries = tempoTotalPorApp.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        Map<String, String> tempoTotalFormatado = new LinkedHashMap<>();
        for (Map.Entry<String, Long> apps : sortedEntries) {
            long minutosTotal = apps.getValue();
            int horas = (int) (minutosTotal / 60);
            int minutos = (int) (minutosTotal % 60);
            tempoTotalFormatado.put(apps.getKey(), formatarHoras(horas, minutos));
        }
        return tempoTotalFormatado;
    }
}
