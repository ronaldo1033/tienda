package com.tiendaintegral.service;
import com.tiendaintegral.dto.request.MovimientoRequest; import com.tiendaintegral.exception.*; import com.tiendaintegral.model.*; import com.tiendaintegral.repository.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List;
@Service
public class InventarioService {
 private final ProductoRepository productos; private final MovimientoInventarioRepository movimientos;
 public InventarioService(ProductoRepository p,MovimientoInventarioRepository m){productos=p;movimientos=m;}
 @Transactional public MovimientoInventario mover(MovimientoRequest r){Producto p=productos.findById(r.productoId()).orElseThrow(()->new NotFoundException("Producto no encontrado."));
  if(r.tipo()==MovimientoRequest.Tipo.ENTRADA)p.setStock(p.getStock()+r.cantidad());else {if(p.getStock()<r.cantidad())throw new BusinessException("Stock insuficiente.");p.setStock(p.getStock()-r.cantidad());}
  productos.save(p);MovimientoInventario m=new MovimientoInventario();m.setProducto(p);m.setCantidad(r.cantidad());m.setMotivo(r.motivo());m.setTipo(r.tipo()==MovimientoRequest.Tipo.ENTRADA?TipoMovimiento.ENTRADA:TipoMovimiento.SALIDA);return movimientos.save(m);}
 public List<MovimientoInventario> historial(){return movimientos.findAll();}
}
