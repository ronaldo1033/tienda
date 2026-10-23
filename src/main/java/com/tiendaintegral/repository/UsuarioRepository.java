package com.tiendaintegral.repository;
import com.tiendaintegral.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { Optional<Usuario> findByUsername(String username); boolean existsByUsername(String username); boolean existsByCorreo(String correo);
}
