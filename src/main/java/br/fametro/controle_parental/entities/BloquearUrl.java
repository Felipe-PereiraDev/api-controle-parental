package br.fametro.controle_parental.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloquearUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private boolean bloqueado;


    @ManyToOne
    @JoinColumn(name = "id_user_filho", nullable = false)
    private UserFilho userFilho;

    @ManyToOne
    @JoinColumn(name = "id_user_responsavel", nullable = false)
    private UserResponsavel userResponsavel;

    public BloquearUrl(String url, UserResponsavel userResponsavel, UserFilho userFilho) {
        this.url = url;
        this.bloqueado = false;
        this.userResponsavel = userResponsavel;
        this.userFilho = userFilho;
    }

    public void mudarEstadoParaTrue() {
        this.bloqueado = true;
    }

}
