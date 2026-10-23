package com.tiendaintegral.model;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDateTime;
@Entity @Table(name="pagos")
public class Pago {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="venta_id",nullable=false) private Venta venta;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private MetodoPago metodo;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal monto;
 @Column(nullable=false) private LocalDateTime fecha=LocalDateTime.now();
 @Enumerated(EnumType.STRING) @Column(nullable=false) private EstadoPago estado=EstadoPago.PENDIENTE;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Venta getVenta(){return venta;} public void setVenta(Venta v){venta=v;}
 public MetodoPago getMetodo(){return metodo;} public void setMetodo(MetodoPago v){metodo=v;} public BigDecimal getMonto(){return monto;} public void setMonto(BigDecimal v){monto=v;}
 public LocalDateTime getFecha(){return fecha;} public void setFecha(LocalDateTime v){fecha=v;} public EstadoPago getEstado(){return estado;} public void setEstado(EstadoPago v){estado=v;}
}
