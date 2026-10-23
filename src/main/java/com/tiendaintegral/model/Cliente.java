package com.tiendaintegral.model;
import jakarta.persistence.*; import jakarta.validation.constraints.*;
@Entity @Table(name="clientes", uniqueConstraints={@UniqueConstraint(columnNames="documento"),@UniqueConstraint(columnNames="correo")})
public class Cliente {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank private String nombres; @NotBlank private String apellidos;
 @NotBlank @Column(nullable=false,unique=true) private String documento;
 private String telefono;
 @Email @NotBlank @Column(nullable=false,unique=true) private String correo;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Estado estado=Estado.ACTIVO;
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getNombres(){return nombres;} public void setNombres(String v){nombres=v;}
 public String getApellidos(){return apellidos;} public void setApellidos(String v){apellidos=v;} public String getDocumento(){return documento;} public void setDocumento(String v){documento=v;}
 public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;} public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
 public Estado getEstado(){return estado;} public void setEstado(Estado v){estado=v;}
}
