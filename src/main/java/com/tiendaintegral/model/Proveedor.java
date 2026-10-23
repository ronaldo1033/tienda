package com.tiendaintegral.model;
import jakarta.persistence.*; import jakarta.validation.constraints.*;
@Entity @Table(name="proveedores", uniqueConstraints=@UniqueConstraint(columnNames="documentoFiscal"))
public class Proveedor {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Column(nullable=false) private String nombre;
 @NotBlank @Column(nullable=false,unique=true,length=20) private String documentoFiscal;
 private String telefono;
 @Email private String correo;
 private String direccion;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Estado estado=Estado.ACTIVO;
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
 public String getDocumentoFiscal(){return documentoFiscal;} public void setDocumentoFiscal(String v){documentoFiscal=v;}
 public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;} public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
 public String getDireccion(){return direccion;} public void setDireccion(String v){direccion=v;} public Estado getEstado(){return estado;} public void setEstado(Estado v){estado=v;}
}
