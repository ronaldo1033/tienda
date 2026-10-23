package com.tiendaintegral.repository;
import com.tiendaintegral.model.DetalleVenta; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}
