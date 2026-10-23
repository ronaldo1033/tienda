package com.tiendaintegral.controller;
import com.tiendaintegral.dto.request.VentaRequest; import com.tiendaintegral.model.Venta; import com.tiendaintegral.service.VentaService;
import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/v1/ventas")
public class VentaController {
 private final VentaService s; public VentaController(VentaService s){this.s=s;}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')") @ResponseStatus(HttpStatus.CREATED) public Venta crear(@Valid @RequestBody VentaRequest r){return s.crear(r);}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<Venta> listar(){return s.listar();}
 @GetMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Venta obtener(@PathVariable Long id){return s.obtener(id);}
 @PatchMapping("/{id}/cancelar") @PreAuthorize("hasRole('ADMIN')") public Venta cancelar(@PathVariable Long id){return s.cancelar(id);}
}
