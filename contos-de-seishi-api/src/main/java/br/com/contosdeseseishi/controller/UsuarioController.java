package br.com.contosdeseseishi.controller;

import br.com.contosdeseseishi.dto.AtualizarPerfilRequest;
import br.com.contosdeseseishi.dto.CadastroUsuarioRequest;
import br.com.contosdeseseishi.dto.UsuarioResponse;
import br.com.contosdeseseishi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody CadastroUsuarioRequest request) {

        UsuarioResponse criado = usuarioService.cadastrar(request);

        URI location = URI.create("/api/usuarios/" + criado.id());
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping
    public List<UsuarioResponse> listarTodos() {
        return usuarioService.listarTodos();
    }

    // Quem sou eu? Só funciona com token válido.
    @GetMapping("/me")
    public UsuarioResponse perfilAtual(Authentication authentication) {
        return usuarioService.buscarPorEmail(authentication.getName());
    }

    // Editar o próprio perfil (nickname e/ou descrição)
    @PutMapping("/me")
    public UsuarioResponse atualizarPerfil(
            Authentication authentication,
            @Valid @RequestBody AtualizarPerfilRequest request) {
        return usuarioService.atualizarPerfil(authentication.getName(), request);
    }
}
