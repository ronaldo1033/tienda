package com.tiendaintegral.controller;
import com.tiendaintegral.dto.request.MovimientoRequest; import com.tiendaintegral.model.MovimientoInventario; import com.tiendaintegral.service.InventarioService;
import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/v1/inventario") @PreAuthorize("hasRole('ADMIN')")
public class InventarioController {
 private final InventarioService s; public InventarioController(InventarioService s){this.s=s;}
 @PostMapping("/movimientos") @ResponseStatus(HttpStatus.CREATED) public MovimientoInventario mover(@Valid @RequestBody MovimientoRequest r){return s.mover(r);}
 @GetMapping("/movimientos") public List<MovimientoInventario> historial(){return s.historial();}
}
