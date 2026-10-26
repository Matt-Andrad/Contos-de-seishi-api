package br.com.contosdeseseishi.dto;

import jakarta.validation.constraints.Size;

public record AtualizarPerfilRequest(

        @Size(min = 3, max = 50, message = "O nickname deve ter entre 3 e 50 caracteres.")
        String nickname,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
        String descricao
) {
}
