package br.com.contosdeseseishi.service;

import br.com.contosdeseseishi.dto.AtualizarPerfilRequest;
import br.com.contosdeseseishi.dto.CadastroUsuarioRequest;
import br.com.contosdeseseishi.dto.UsuarioResponse;
import br.com.contosdeseseishi.entity.TipoUsuario;
import br.com.contosdeseseishi.entity.Usuario;
import br.com.contosdeseseishi.exception.RecursoNaoEncontradoException;
import br.com.contosdeseseishi.exception.RegraNegocioException;
import br.com.contosdeseseishi.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final SeguidorService seguidorService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          SeguidorService seguidorService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.seguidorService = seguidorService;
    }

    @Transactional
    public UsuarioResponse cadastrar(CadastroUsuarioRequest request) {

        if (usuarioRepository.existsByNickname(request.nickname())) {
            throw new RegraNegocioException("Este nickname já está em uso.");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraNegocioException("Este e-mail já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNickname(request.nickname());
        usuario.setEmail(request.email());
        // BCrypt: a senha em texto puro NUNCA toca o banco
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setTipo(TipoUsuario.USUARIO); // cadastro público nunca cria admin
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);
        return paraResponse(salvo);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com id " + id + "."));
        return paraResponse(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com e-mail " + email + "."));
        return paraResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::paraResponse)
                .toList();
    }

    @Transactional
    public UsuarioResponse atualizarPerfil(String emailDoLogado, AtualizarPerfilRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailDoLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        // Só verifica unicidade se o nickname realmente mudou
        if (request.nickname() != null && !request.nickname().equals(usuario.getNickname())) {
            if (usuarioRepository.existsByNickname(request.nickname())) {
                throw new RegraNegocioException("Este nickname já está em uso.");
            }
            usuario.setNickname(request.nickname());
        }

        if (request.descricao() != null) {
            usuario.setDescricao(request.descricao());
        }

        return paraResponse(usuarioRepository.save(usuario));
    }

    // Converte Entity -> DTO incluindo os contadores de seguidores
    private UsuarioResponse paraResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNickname(),
                usuario.getEmail(),
                usuario.getTipo(),
                usuario.getDescricao(),
                usuario.getCriadoEm(),
                seguidorService.contarSeguidores(usuario.getId()),
                seguidorService.contarSeguindo(usuario.getId())
        );
    }
}
