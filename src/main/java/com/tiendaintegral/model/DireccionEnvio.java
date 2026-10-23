package com.tiendaintegral.model;
import jakarta.persistence.*; import jakarta.validation.constraints.NotBlank;
@Entity @Table(name="direcciones_envio")
public class DireccionEnvio {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="cliente_id",nullable=false) private Cliente cliente;
 @NotBlank private String direccion; @NotBlank private String distrito; @NotBlank private String ciudad; @NotBlank private String departamento;
 private String referencia; @Column(nullable=false) private boolean principal=false;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Cliente getCliente(){return cliente;} public void setCliente(Cliente v){cliente=v;}
 public String getDireccion(){return direccion;} public void setDireccion(String v){direccion=v;} public String getDistrito(){return distrito;} public void setDistrito(String v){distrito=v;}
 public String getCiudad(){return ciudad;} public void setCiudad(String v){ciudad=v;} public String getDepartamento(){return departamento;} public void setDepartamento(String v){departamento=v;}
 public String getReferencia(){return referencia;} public void setReferencia(String v){referencia=v;} public boolean isPrincipal(){return principal;} public void setPrincipal(boolean v){principal=v;}
}
