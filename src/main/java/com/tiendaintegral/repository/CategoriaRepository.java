package com.tiendaintegral.repository;
import com.tiendaintegral.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface CategoriaRepository extends JpaRepository<Categoria, Long> { boolean existsByNombreIgnoreCase(String nombre);
}
