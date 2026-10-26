package br.com.contosdeseseishi.dto;

public record LoginResponse(
        String token,
        String tipo,
        String nickname
) {
}
