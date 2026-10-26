package br.com.contosdeseseishi.dto;

import br.com.contosdeseseishi.entity.Usuario;

public record UsuarioResumoResponse(
        Long id,
        String nickname
) {
    public static UsuarioResumoResponse de(Usuario usuario) {
        return new UsuarioResumoResponse(usuario.getId(), usuario.getNickname());
    }
}
