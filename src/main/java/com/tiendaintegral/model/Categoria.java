package com.tiendaintegral.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
@Entity @Table(name="categorias", uniqueConstraints=@UniqueConstraint(columnNames="nombre"))
public class Categoria {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Column(nullable=false, unique=true, length=100) private String nombre;
 @Column(length=500) private String descripcion;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Estado estado=Estado.ACTIVO;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
 public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
 public Estado getEstado(){return estado;} public void setEstado(Estado v){estado=v;}
}
