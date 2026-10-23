package com.tiendaintegral.controller;
import com.tiendaintegral.model.*; import com.tiendaintegral.service.PagoService; import jakarta.validation.constraints.*; import org.springframework.http.HttpStatus; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal; import java.util.List;
@RestController @RequestMapping("/api/v1/pagos")
public class PagoController {
 private final PagoService s; public PagoController(PagoService s){this.s=s;}
 public record PagoRequest(@NotNull Long ventaId,@NotNull MetodoPago metodo,@NotNull @DecimalMin("0.01") BigDecimal monto,@NotNull Boolean aprobar){}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')") @ResponseStatus(HttpStatus.CREATED) public Pago pagar(@RequestBody @jakarta.validation.Valid PagoRequest r){return s.simular(r.ventaId(),r.metodo(),r.monto(),r.aprobar());}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<Pago> listar(){return s.listar();}
}
