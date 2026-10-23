package com.tiendaintegral.model;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.*;
@Entity @Table(name="ventas")
public class Venta {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="cliente_id",nullable=false) private Cliente cliente;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="usuario_id") private Usuario usuario;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="direccion_id") private DireccionEnvio direccionEnvio;
 @Column(nullable=false) private LocalDateTime fecha=LocalDateTime.now();
 @Column(nullable=false,precision=12,scale=2) private BigDecimal subtotal=BigDecimal.ZERO;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal total=BigDecimal.ZERO;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private EstadoVenta estado=EstadoVenta.PENDIENTE;
 @OneToMany(mappedBy="venta",cascade=CascadeType.ALL,orphanRemoval=true) private List<DetalleVenta> detalles=new ArrayList<>();
 public Long getId(){return id;} public void setId(Long v){id=v;} public Cliente getCliente(){return cliente;} public void setCliente(Cliente v){cliente=v;}
 public Usuario getUsuario(){return usuario;} public void setUsuario(Usuario v){usuario=v;} public DireccionEnvio getDireccionEnvio(){return direccionEnvio;} public void setDireccionEnvio(DireccionEnvio v){direccionEnvio=v;}
 public LocalDateTime getFecha(){return fecha;} public void setFecha(LocalDateTime v){fecha=v;} public BigDecimal getSubtotal(){return subtotal;} public void setSubtotal(BigDecimal v){subtotal=v;}
 public BigDecimal getTotal(){return total;} public void setTotal(BigDecimal v){total=v;} public EstadoVenta getEstado(){return estado;} public void setEstado(EstadoVenta v){estado=v;}
 public List<DetalleVenta> getDetalles(){return detalles;} public void setDetalles(List<DetalleVenta> v){detalles=v;}
}
