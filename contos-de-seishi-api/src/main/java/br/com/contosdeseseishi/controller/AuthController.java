package br.com.contosdeseseishi.controller;

import br.com.contosdeseseishi.dto.LoginRequest;
import br.com.contosdeseseishi.dto.LoginResponse;
import br.com.contosdeseseishi.entity.Usuario;
import br.com.contosdeseseishi.repository.UsuarioRepository;
import br.com.contosdeseseishi.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        try {
            // Tenta autenticar: busca o usuário pelo e-mail e compara o BCrypt
            Authentication autenticacao = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

            UserDetails usuarioAutenticado = (UserDetails) autenticacao.getPrincipal();
            String token = jwtService.gerarToken(usuarioAutenticado);

            Usuario usuario = usuarioRepository.findByEmail(request.email()).orElseThrow();

            return new LoginResponse(token, usuario.getTipo().name(), usuario.getNickname());

        } catch (BadCredentialsException e) {
            // 401 genérico de propósito: não revelamos se o e-mail existe ou a senha está errada
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
        }
    }
}
