package com.tiendaintegral.controller;
import com.tiendaintegral.model.*; import com.tiendaintegral.repository.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal; import java.util.*;
@RestController @RequestMapping("/api/v1/reportes") @PreAuthorize("hasRole('ADMIN')")
public class ReporteController {
 private final VentaRepository ventas; private final ProductoRepository productos; private final PagoRepository pagos; private final MovimientoInventarioRepository movimientos;
 public ReporteController(VentaRepository v,ProductoRepository p,PagoRepository pa,MovimientoInventarioRepository m){ventas=v;productos=p;pagos=pa;movimientos=m;}
 @GetMapping("/resumen") public Map<String,Object> resumen(){List<Venta> vs=ventas.findAll();BigDecimal ingresos=pagos.findAll().stream().filter(p->p.getEstado()==EstadoPago.APROBADO).map(Pago::getMonto).reduce(BigDecimal.ZERO,BigDecimal::add);
  Map<String,Long> porEstado=new LinkedHashMap<>();for(EstadoVenta e:EstadoVenta.values())porEstado.put(e.name(),vs.stream().filter(v->v.getEstado()==e).count());
  return Map.of("cantidadVentas",vs.size(),"ventasPorEstado",porEstado,"ingresosPagosAprobados",ingresos,"productosStockBajo",productos.findAll().stream().filter(p->p.getStock()<=p.getStockMinimo()).count(),"movimientosInventario",movimientos.count());}
}
