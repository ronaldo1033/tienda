package com.tiendaintegral.repository;
import com.tiendaintegral.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface PagoRepository extends JpaRepository<Pago, Long> { boolean existsByVentaIdAndEstado(Long ventaId, EstadoPago estado);
}
