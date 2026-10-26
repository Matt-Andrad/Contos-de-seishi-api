package br.com.contosdeseseishi.repository;

import br.com.contosdeseseishi.entity.Seguidor;
import br.com.contosdeseseishi.entity.SeguidorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguidorRepository extends JpaRepository<Seguidor, SeguidorId> {

    long countBySeguidoId(Long seguidoId);   // quantos seguem o usuário X

    long countBySeguidorId(Long seguidorId); // quantos o usuário X segue

    boolean existsById(SeguidorId id);       // já segue?

    List<Seguidor> findBySeguidoId(Long seguidoId);   // quem segue X

    List<Seguidor> findBySeguidorId(Long seguidorId); // quem X segue
}
