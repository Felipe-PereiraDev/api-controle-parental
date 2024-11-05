package br.fametro.controle_parental.entities;

import br.fametro.controle_parental.entities.enums.TipoEvento;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tb_logs")
@Data
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_acesso", nullable = false)
    private LocalDate dataAcesso;

    @Column(name = "tipo_evento", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;

    // falta adicionar not null
    private String detalhes;

    @Column(nullable = false)
    private Long duracao;

    private LocalTime hora;
    @ManyToOne
    @JoinColumn(name = "id_user_filho", nullable = false)
    private UserFilho userFilho;

    public Log (LocalDate dataAcesso, TipoEvento tipoEvento, String detalhes, Long duracao, UserFilho userFilho, LocalTime hora) {
        this.dataAcesso = dataAcesso;
        this.tipoEvento = tipoEvento;
        this.detalhes = detalhes.replace(".exe", "");
        this.duracao = duracao;
        this.userFilho = userFilho;
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", dataAcesso=" + dataAcesso +
                ", tipoEvento=" + tipoEvento +
                ", detalhes='" + detalhes + '\'' +
                ", duracao=" + duracao +
                ", userFilho=" + userFilho.getNome() +
                '}';
    }
}
