package com.tiendaintegral.model;
import jakarta.persistence.*; import java.math.BigDecimal;
@Entity @Table(name="detalles_venta")
public class DetalleVenta {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="venta_id",nullable=false) private Venta venta;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="producto_id",nullable=false) private Producto producto;
 @Column(nullable=false) private int cantidad;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal precioUnitario;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal subtotal;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Venta getVenta(){return venta;} public void setVenta(Venta v){venta=v;}
 public Producto getProducto(){return producto;} public void setProducto(Producto v){producto=v;} public int getCantidad(){return cantidad;} public void setCantidad(int v){cantidad=v;}
 public BigDecimal getPrecioUnitario(){return precioUnitario;} public void setPrecioUnitario(BigDecimal v){precioUnitario=v;} public BigDecimal getSubtotal(){return subtotal;} public void setSubtotal(BigDecimal v){subtotal=v;}
}
