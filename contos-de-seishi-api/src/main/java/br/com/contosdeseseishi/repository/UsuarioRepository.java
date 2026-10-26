package br.com.contosdeseseishi.repository;

import br.com.contosdeseseishi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
