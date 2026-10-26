package br.com.contosdeseseishi.controller;

import br.com.contosdeseseishi.dto.UsuarioResumoResponse;
import br.com.contosdeseseishi.service.SeguidorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class SeguidorController {

    private final SeguidorService seguidorService;

    public SeguidorController(SeguidorService seguidorService) {
        this.seguidorService = seguidorService;
    }

    @PostMapping("/{id}/seguir")
    public ResponseEntity<Void> seguir(@PathVariable Long id, Authentication authentication) {
        seguidorService.seguir(authentication.getName(), id);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201, sem corpo
    }

    @DeleteMapping("/{id}/seguir")
    public ResponseEntity<Void> deixarDeSeguir(@PathVariable Long id, Authentication authentication) {
        seguidorService.deixarDeSeguir(authentication.getName(), id);
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/{id}/seguidores")
    public List<UsuarioResumoResponse> listarSeguidores(@PathVariable Long id) {
        return seguidorService.listarSeguidores(id);
    }

    @GetMapping("/{id}/seguindo")
    public List<UsuarioResumoResponse> listarSeguindo(@PathVariable Long id) {
        return seguidorService.listarSeguindo(id);
    }
}
