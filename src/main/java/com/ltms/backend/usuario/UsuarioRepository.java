package com.ltms.backend.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findById(Integer idUsuario);
    Optional<Usuario> findByEmail(String email);

    @Query(
            """
            SELECT usuario
            FROM Usuario usuario
            WHERE usuario.enabled = true
            """
    )
    Page<Usuario> findAllUsers(Pageable pageable);
}
