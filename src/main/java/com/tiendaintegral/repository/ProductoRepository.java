package com.tiendaintegral.repository;
import com.tiendaintegral.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ProductoRepository extends JpaRepository<Producto, Long> { List<Producto> findByNombreContainingIgnoreCase(String nombre); List<Producto> findByStockLessThanEqual(int limite);
}
