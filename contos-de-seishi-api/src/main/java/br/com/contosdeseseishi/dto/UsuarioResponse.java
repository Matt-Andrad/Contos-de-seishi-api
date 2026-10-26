package br.com.contosdeseseishi.dto;

import br.com.contosdeseseishi.entity.TipoUsuario;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nickname,
        String email,
        TipoUsuario tipo,
        String descricao,
        LocalDateTime criadoEm,
        long seguidores,
        long seguindo
) {
}
