package com.tiendaintegral.repository;
import com.tiendaintegral.model.MovimientoInventario; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
}
