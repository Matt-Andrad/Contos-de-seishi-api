package br.com.contosdeseseishi.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SeguidorId implements Serializable {

    private Long seguidorId;
    private Long seguidoId;

    public SeguidorId() {
    }

    public SeguidorId(Long seguidorId, Long seguidoId) {
        this.seguidorId = seguidorId;
        this.seguidoId = seguidoId;
    }

    public Long getSeguidorId() { return seguidorId; }
    public Long getSeguidoId() { return seguidoId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeguidorId outro)) return false;
        return Objects.equals(seguidorId, outro.seguidorId)
                && Objects.equals(seguidoId, outro.seguidoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seguidorId, seguidoId);
    }
}
