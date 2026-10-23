package com.tiendaintegral.service;
import com.tiendaintegral.exception.*; import com.tiendaintegral.model.*; import com.tiendaintegral.repository.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.math.BigDecimal; import java.util.*;
@Service
public class PagoService {
 private final PagoRepository pagos; private final VentaRepository ventas;
 public PagoService(PagoRepository p,VentaRepository v){pagos=p;ventas=v;}
 @Transactional public Pago simular(Long ventaId,MetodoPago metodo,BigDecimal monto,boolean aprobar){Venta v=ventas.findById(ventaId).orElseThrow(()->new NotFoundException("Venta no encontrada."));
  if(v.getEstado()==EstadoVenta.CANCELADA)throw new BusinessException("No se puede pagar una venta cancelada.");
  if(pagos.existsByVentaIdAndEstado(ventaId,EstadoPago.APROBADO))throw new BusinessException("La venta ya tiene un pago aprobado.");
  if(monto==null||monto.compareTo(v.getTotal())!=0)throw new BusinessException("El importe debe coincidir con el total de la venta.");
  Pago p=new Pago();p.setVenta(v);p.setMetodo(metodo);p.setMonto(monto);p.setEstado(aprobar?EstadoPago.APROBADO:EstadoPago.RECHAZADO);
  if(aprobar)v.setEstado(EstadoVenta.PAGADA);ventas.save(v);return pagos.save(p);
 }
 public List<Pago> listar(){return pagos.findAll();}
}
