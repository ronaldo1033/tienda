package com.tiendaintegral.repository;
import com.tiendaintegral.model.Proveedor; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}
