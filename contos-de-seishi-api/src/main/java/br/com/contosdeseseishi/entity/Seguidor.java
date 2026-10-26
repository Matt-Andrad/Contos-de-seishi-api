package br.com.contosdeseseishi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguidores")
public class Seguidor {

    @EmbeddedId
    private SeguidorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seguidorId")
    @JoinColumn(name = "seguidor_id")
    private Usuario seguidor; // quem segue

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seguidoId")
    @JoinColumn(name = "seguido_id")
    private Usuario seguido;  // quem é seguido

    @Column(name = "seguido_em", nullable = false, updatable = false)
    private LocalDateTime seguidoEm;

    protected Seguidor() {
    }

    public Seguidor(Usuario seguidor, Usuario seguido) {
        this.seguidor = seguidor;
        this.seguido = seguido;
        this.id = new SeguidorId(seguidor.getId(), seguido.getId());
    }

    @PrePersist
    void aoSeguir() {
        this.seguidoEm = LocalDateTime.now();
    }

    public SeguidorId getId() { return id; }
    public Usuario getSeguidor() { return seguidor; }
    public Usuario getSeguido() { return seguido; }
    public LocalDateTime getSeguidoEm() { return seguidoEm; }
}
