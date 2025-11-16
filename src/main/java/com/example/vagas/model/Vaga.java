package com.example.vagas.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vagas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaga {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String uf;

    @Column(columnDefinition = "text")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", length = 30)
    private TipoContrato tipoContrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "senioridade", length = 30)
    private Senioridade senioridade;

    @Column(name = "faixa_salarial", length = 64)
    private String faixaSalarial;

    @Enumerated(EnumType.STRING)
    @Column(name = "modelo_trabalho", length = 20)
    private ModeloTrabalho modeloTrabalho;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "skills_obrigatorias", columnDefinition = "jsonb")
    private List<String> skillsObrigatorias;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "skills_desejaveis", columnDefinition = "jsonb")
    private List<String> skillsDesejaveis;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public enum TipoContrato {
        CLT, PJ, FREELANCER, ESTAGIO, TEMPORARIO, INTERNSHIP
    }

    public enum ModeloTrabalho {
        REMOTO, HIBRIDO, PRESENCIAL
    }

    public enum Senioridade {
        ESTAGIO, JUNIOR, PLENO, SENIOR, ESPECIALISTA, PRINCIPAL, STAFF
    }
}
