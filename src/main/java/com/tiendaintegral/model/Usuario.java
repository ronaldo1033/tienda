package com.tiendaintegral.model;
import jakarta.persistence.*; import jakarta.validation.constraints.*;
@Entity @Table(name="usuarios", uniqueConstraints={@UniqueConstraint(columnNames="username"),@UniqueConstraint(columnNames="correo")})
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Column(nullable=false,unique=true) private String username;
 @Email @NotBlank @Column(nullable=false,unique=true) private String correo;
 @Column(nullable=false) private String passwordHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Rol rol=Rol.CLIENTE;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Estado estado=Estado.ACTIVO;
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getUsername(){return username;} public void setUsername(String v){username=v;}
 public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;} public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
 public Rol getRol(){return rol;} public void setRol(Rol v){rol=v;} public Estado getEstado(){return estado;} public void setEstado(Estado v){estado=v;}
}
