package com.tiendaintegral.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="movimientos_inventario")
public class MovimientoInventario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="producto_id",nullable=false) private Producto producto;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private TipoMovimiento tipo;
 @Column(nullable=false) private int cantidad;
 @Column(nullable=false,length=300) private String motivo;
 @Column(nullable=false) private LocalDateTime fecha=LocalDateTime.now();
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="usuario_id") private Usuario usuario;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Producto getProducto(){return producto;} public void setProducto(Producto v){producto=v;}
 public TipoMovimiento getTipo(){return tipo;} public void setTipo(TipoMovimiento v){tipo=v;} public int getCantidad(){return cantidad;} public void setCantidad(int v){cantidad=v;}
 public String getMotivo(){return motivo;} public void setMotivo(String v){motivo=v;} public LocalDateTime getFecha(){return fecha;} public void setFecha(LocalDateTime v){fecha=v;}
 public Usuario getUsuario(){return usuario;} public void setUsuario(Usuario v){usuario=v;}
}
