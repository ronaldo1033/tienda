package com.tiendaintegral.model;
import jakarta.persistence.*; import jakarta.validation.constraints.*; import java.math.BigDecimal;
@Entity @Table(name="productos", indexes=@Index(columnList="nombre"))
public class Producto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Column(nullable=false) private String nombre;
 @Column(length=1000) private String descripcion;
 @NotNull @DecimalMin("0.00") @Column(nullable=false,precision=12,scale=2) private BigDecimal precio;
 @Min(0) @Column(nullable=false) private int stock;
 @Min(0) @Column(nullable=false) private int stockMinimo=5;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Estado estado=Estado.ACTIVO;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="categoria_id",nullable=false) private Categoria categoria;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="proveedor_id") private Proveedor proveedor;
 @Version private Long version;
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
 public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;} public BigDecimal getPrecio(){return precio;} public void setPrecio(BigDecimal v){precio=v;}
 public int getStock(){return stock;} public void setStock(int v){stock=v;} public int getStockMinimo(){return stockMinimo;} public void setStockMinimo(int v){stockMinimo=v;}
 public Estado getEstado(){return estado;} public void setEstado(Estado v){estado=v;} public Categoria getCategoria(){return categoria;} public void setCategoria(Categoria v){categoria=v;}
 public Proveedor getProveedor(){return proveedor;} public void setProveedor(Proveedor v){proveedor=v;}
}
