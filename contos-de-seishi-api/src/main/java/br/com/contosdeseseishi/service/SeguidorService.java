package br.com.contosdeseseishi.service;

import br.com.contosdeseseishi.dto.UsuarioResumoResponse;
import br.com.contosdeseseishi.entity.Seguidor;
import br.com.contosdeseseishi.entity.SeguidorId;
import br.com.contosdeseseishi.entity.Usuario;
import br.com.contosdeseseishi.exception.RecursoNaoEncontradoException;
import br.com.contosdeseseishi.exception.RegraNegocioException;
import br.com.contosdeseseishi.repository.SeguidorRepository;
import br.com.contosdeseseishi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeguidorService {

    private final SeguidorRepository seguidorRepository;
    private final UsuarioRepository usuarioRepository;

    public SeguidorService(SeguidorRepository seguidorRepository,
                           UsuarioRepository usuarioRepository) {
        this.seguidorRepository = seguidorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void seguir(String emailDoLogado, Long idDoSeguido) {
        Usuario seguidor = buscarPorEmail(emailDoLogado);
        Usuario seguido = buscarPorId(idDoSeguido);

        // Regra 1: não pode seguir a si mesmo
        if (seguidor.getId().equals(seguido.getId())) {
            throw new RegraNegocioException("Você não pode seguir a si mesmo.");
        }

        // Regra 2: não pode seguir duas vezes
        if (seguidorRepository.existsById(new SeguidorId(seguidor.getId(), seguido.getId()))) {
            throw new RegraNegocioException("Você já segue este usuário.");
        }

        seguidorRepository.save(new Seguidor(seguidor, seguido));
    }

    @Transactional
    public void deixarDeSeguir(String emailDoLogado, Long idDoSeguido) {
        Usuario seguidor = buscarPorEmail(emailDoLogado);
        SeguidorId id = new SeguidorId(seguidor.getId(), idDoSeguido);

        if (!seguidorRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Você não segue este usuário.");
        }

        seguidorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResumoResponse> listarSeguidores(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com id " + usuarioId + ".");
        }
        return seguidorRepository.findBySeguidoId(usuarioId).stream()
                .map(s -> UsuarioResumoResponse.de(s.getSeguidor()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioResumoResponse> listarSeguindo(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com id " + usuarioId + ".");
        }
        return seguidorRepository.findBySeguidorId(usuarioId).stream()
                .map(s -> UsuarioResumoResponse.de(s.getSeguido()))
                .toList();
    }

    // Contadores (usados no perfil público)
    @Transactional(readOnly = true)
    public long contarSeguidores(Long usuarioId) {
        return seguidorRepository.countBySeguidoId(usuarioId);
    }

    @Transactional(readOnly = true)
    public long contarSeguindo(Long usuarioId) {
        return seguidorRepository.countBySeguidorId(usuarioId);
    }

    private Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    private Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com id " + id + "."));
    }
}
