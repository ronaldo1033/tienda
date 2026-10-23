package com.tiendaintegral.controller;
import com.tiendaintegral.model.*; import com.tiendaintegral.service.CatalogoService; import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1") 
public class CatalogoController {
 private final CatalogoService s; public CatalogoController(CatalogoService s){this.s=s;}
 @GetMapping("/categorias") public List<Categoria> categorias(){return s.categorias();}
 @GetMapping("/categorias/{id}") public Categoria categoria(@PathVariable Long id){return s.categoria(id);}
 @PostMapping("/categorias") @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.CREATED) public Categoria crearCategoria(@Valid @RequestBody Categoria x){return s.guardarCategoria(x);}
 @PutMapping("/categorias/{id}") @PreAuthorize("hasRole('ADMIN')") public Categoria actualizarCategoria(@PathVariable Long id,@Valid @RequestBody Categoria x){return s.actualizarCategoria(id,x);}
 @PatchMapping("/categorias/{id}/desactivar") @PreAuthorize("hasRole('ADMIN')") public Categoria desactivarCategoria(@PathVariable Long id){Categoria c=s.categoria(id);c.setEstado(Estado.INACTIVO);return s.actualizarCategoria(id,c);}
 @GetMapping("/proveedores") @PreAuthorize("hasRole('ADMIN')") public List<Proveedor> proveedores(){return s.proveedores();}
 @GetMapping("/proveedores/{id}") @PreAuthorize("hasRole('ADMIN')") public Proveedor proveedor(@PathVariable Long id){return s.proveedor(id);}
 @PostMapping("/proveedores") @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.CREATED) public Proveedor crearProveedor(@Valid @RequestBody Proveedor x){return s.guardarProveedor(x);}
 @PutMapping("/proveedores/{id}") @PreAuthorize("hasRole('ADMIN')") public Proveedor actualizarProveedor(@PathVariable Long id,@Valid @RequestBody Proveedor x){return s.actualizarProveedor(id,x);}
 @GetMapping("/productos") public List<Producto> productos(){return s.productos();}
 @GetMapping("/productos/{id}") public Producto producto(@PathVariable Long id){return s.producto(id);}
 @PostMapping("/productos") @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.CREATED) public Producto crearProducto(@Valid @RequestBody Producto x){return s.guardarProducto(x);}
 @PutMapping("/productos/{id}") @PreAuthorize("hasRole('ADMIN')") public Producto actualizarProducto(@PathVariable Long id,@Valid @RequestBody Producto x){return s.actualizarProducto(id,x);}
 @GetMapping("/productos/stock-bajo") @PreAuthorize("hasRole('ADMIN')") public List<Producto> stockBajo(){return s.stockBajo();}
 @GetMapping("/clientes") @PreAuthorize("hasRole('ADMIN')") public List<Cliente> clientes(){return s.clientes();}
 @GetMapping("/clientes/{id}") @PreAuthorize("hasRole('ADMIN')") public Cliente cliente(@PathVariable Long id){return s.cliente(id);}
 @PostMapping("/clientes") @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')") @ResponseStatus(HttpStatus.CREATED) public Cliente crearCliente(@Valid @RequestBody Cliente x){return s.guardarCliente(x);}
 @PutMapping("/clientes/{id}") @PreAuthorize("hasRole('ADMIN')") public Cliente actualizarCliente(@PathVariable Long id,@Valid @RequestBody Cliente x){return s.actualizarCliente(id,x);}
}
