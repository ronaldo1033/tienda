package com.tiendaintegral.service;
import com.tiendaintegral.dto.request.VentaRequest; import com.tiendaintegral.exception.*; import com.tiendaintegral.model.*; import com.tiendaintegral.repository.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.math.BigDecimal; import java.util.*;
@Service
public class VentaService {
 private final VentaRepository ventas; private final ClienteRepository clientes; private final ProductoRepository productos; private final DireccionEnvioRepository direcciones; private final MovimientoInventarioRepository movimientos;
 public VentaService(VentaRepository v,ClienteRepository c,ProductoRepository p,DireccionEnvioRepository d,MovimientoInventarioRepository m){ventas=v;clientes=c;productos=p;direcciones=d;movimientos=m;}
 @Transactional public Venta crear(VentaRequest r){Cliente c=clientes.findById(r.clienteId()).orElseThrow(()->new NotFoundException("Cliente no encontrado."));
  Venta v=new Venta();v.setCliente(c); if(r.direccionId()!=null){DireccionEnvio de=direcciones.findById(r.direccionId()).orElseThrow(()->new NotFoundException("Dirección no encontrada."));if(!de.getCliente().getId().equals(c.getId()))throw new BusinessException("La dirección no pertenece al cliente.");v.setDireccionEnvio(de);}
  BigDecimal total=BigDecimal.ZERO;
  for(VentaRequest.Item item:r.items()){Producto p=productos.findById(item.productoId()).orElseThrow(()->new NotFoundException("Producto no encontrado: "+item.productoId()));
   if(p.getEstado()!=Estado.ACTIVO)throw new BusinessException("El producto está inactivo: "+p.getNombre());if(p.getStock()<item.cantidad())throw new BusinessException("Stock insuficiente para "+p.getNombre());
   p.setStock(p.getStock()-item.cantidad());productos.save(p);BigDecimal sub=p.getPrecio().multiply(BigDecimal.valueOf(item.cantidad()));
   DetalleVenta d=new DetalleVenta();d.setVenta(v);d.setProducto(p);d.setCantidad(item.cantidad());d.setPrecioUnitario(p.getPrecio());d.setSubtotal(sub);v.getDetalles().add(d);total=total.add(sub);
   MovimientoInventario mov=new MovimientoInventario();mov.setProducto(p);mov.setTipo(TipoMovimiento.SALIDA);mov.setCantidad(item.cantidad());mov.setMotivo("Venta pendiente");movimientos.save(mov);
  }
  v.setSubtotal(total);v.setTotal(total);return ventas.save(v);
 }
 public List<Venta> listar(){return ventas.findAll();} public Venta obtener(Long id){return ventas.findById(id).orElseThrow(()->new NotFoundException("Venta no encontrada."));}
 @Transactional public Venta cancelar(Long id){Venta v=obtener(id);if(v.getEstado()==EstadoVenta.CANCELADA)throw new BusinessException("La venta ya fue cancelada.");if(v.getEstado()==EstadoVenta.PAGADA)throw new BusinessException("No se puede cancelar una venta pagada desde este endpoint.");
  for(DetalleVenta d:v.getDetalles()){Producto p=d.getProducto();p.setStock(p.getStock()+d.getCantidad());productos.save(p);MovimientoInventario m=new MovimientoInventario();m.setProducto(p);m.setTipo(TipoMovimiento.DEVOLUCION);m.setCantidad(d.getCantidad());m.setMotivo("Cancelación de venta "+id);movimientos.save(m);}
  v.setEstado(EstadoVenta.CANCELADA);return ventas.save(v);
 }
}
