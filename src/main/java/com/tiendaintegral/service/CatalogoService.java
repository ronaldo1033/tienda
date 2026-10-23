package com.tiendaintegral.service;
import com.tiendaintegral.exception.NotFoundException; import com.tiendaintegral.model.*; import com.tiendaintegral.repository.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
@Service
public class CatalogoService {
 private final CategoriaRepository categorias; private final ProveedorRepository proveedores; private final ProductoRepository productos; private final ClienteRepository clientes;
 public CatalogoService(CategoriaRepository c,ProveedorRepository p,ProductoRepository pr,ClienteRepository cl){categorias=c;proveedores=p;productos=pr;clientes=cl;}
 public List<Categoria> categorias(){return categorias.findAll();} public Categoria categoria(Long id){return categorias.findById(id).orElseThrow(()->new NotFoundException("Categoría no encontrada."));}
 @Transactional public Categoria guardarCategoria(Categoria x){return categorias.save(x);} @Transactional public Categoria actualizarCategoria(Long id,Categoria x){Categoria old=categoria(id);old.setNombre(x.getNombre());old.setDescripcion(x.getDescripcion());if(x.getEstado()!=null)old.setEstado(x.getEstado());return categorias.save(old);}
 public List<Proveedor> proveedores(){return proveedores.findAll();} public Proveedor proveedor(Long id){return proveedores.findById(id).orElseThrow(()->new NotFoundException("Proveedor no encontrado."));}
 @Transactional public Proveedor guardarProveedor(Proveedor x){return proveedores.save(x);} @Transactional public Proveedor actualizarProveedor(Long id,Proveedor x){Proveedor o=proveedor(id);o.setNombre(x.getNombre());o.setDocumentoFiscal(x.getDocumentoFiscal());o.setTelefono(x.getTelefono());o.setCorreo(x.getCorreo());o.setDireccion(x.getDireccion());o.setEstado(x.getEstado());return proveedores.save(o);}
 public List<Producto> productos(){return productos.findAll();} public Producto producto(Long id){return productos.findById(id).orElseThrow(()->new NotFoundException("Producto no encontrado."));}
 @Transactional public Producto guardarProducto(Producto x){return productos.save(x);}
 @Transactional public Producto actualizarProducto(Long id,Producto x){Producto o=producto(id);o.setNombre(x.getNombre());o.setDescripcion(x.getDescripcion());o.setPrecio(x.getPrecio());o.setStockMinimo(x.getStockMinimo());o.setEstado(x.getEstado());o.setCategoria(x.getCategoria());o.setProveedor(x.getProveedor());return productos.save(o);}
 public List<Producto> stockBajo(){return productos.findAll().stream().filter(p->p.getStock()<=p.getStockMinimo()).toList();}
 public List<Cliente> clientes(){return clientes.findAll();} public Cliente cliente(Long id){return clientes.findById(id).orElseThrow(()->new NotFoundException("Cliente no encontrado."));}
 @Transactional public Cliente guardarCliente(Cliente x){return clientes.save(x);}
 @Transactional public Cliente actualizarCliente(Long id,Cliente x){Cliente o=cliente(id);o.setNombres(x.getNombres());o.setApellidos(x.getApellidos());o.setDocumento(x.getDocumento());o.setTelefono(x.getTelefono());o.setCorreo(x.getCorreo());o.setEstado(x.getEstado());return clientes.save(o);}
}
