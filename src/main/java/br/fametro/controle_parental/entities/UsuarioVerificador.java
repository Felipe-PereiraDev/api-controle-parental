package br.fametro.controle_parental.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario_verificador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioVerificador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private Instant dataExpiracao;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", unique = true)
    private UserResponsavel userResponsavel;

    @ManyToOne
    @JoinColumn(name = "id_user_filho", referencedColumnName = "id", unique = true)
    private UserFilho userFilho;
}
