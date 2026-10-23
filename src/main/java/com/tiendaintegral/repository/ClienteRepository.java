package com.tiendaintegral.repository;
import com.tiendaintegral.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ClienteRepository extends JpaRepository<Cliente, Long> { Optional<Cliente> findByDocumento(String documento); Optional<Cliente> findByCorreo(String correo);
}
