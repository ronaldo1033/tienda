package com.tiendaintegral.config;

import com.tiendaintegral.model.*;
import com.tiendaintegral.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Datos de demostración para desarrollo local. Es idempotente: no duplica registros
 * si la aplicación se reinicia. Desactivar con app.demo-data.enabled=false.
 */
@Component
public class DemoDataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    private final ProveedorRepository proveedores;
    private final ProductoRepository productos;
    private final ClienteRepository clientes;
    private final PasswordEncoder encoder;

    @Value("${app.demo-data.enabled:true}")
    private boolean enabled;

    public DemoDataInitializer(UsuarioRepository usuarios, CategoriaRepository categorias,
            ProveedorRepository proveedores, ProductoRepository productos,
            ClienteRepository clientes, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.proveedores = proveedores;
        this.productos = productos;
        this.clientes = clientes;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled) return;
        crearUsuario("admin.demo", "admin.demo@tiendaintegral.local", "Admin123!", Rol.ADMIN);
        crearUsuario("cliente.demo", "cliente.demo@tiendaintegral.local", "Cliente123!", Rol.CLIENTE);

        Categoria abarrotes = categoria("Abarrotes", "Productos básicos de despensa");
        Categoria bebidas = categoria("Bebidas", "Bebidas y refrescos");
        Categoria limpieza = categoria("Limpieza", "Artículos de limpieza del hogar");

        Proveedor proveedor = proveedores.findAll().stream()
                .filter(p -> "DEMO-201".equals(p.getDocumentoFiscal())).findFirst().orElseGet(() -> {
                    Proveedor p = new Proveedor();
                    p.setNombre("Distribuidora Demo");
                    p.setDocumentoFiscal("DEMO-201");
                    p.setTelefono("999111222");
                    p.setCorreo("ventas@proveedor-demo.local");
                    p.setDireccion("Lima, Perú");
                    p.setEstado(Estado.ACTIVO);
                    return proveedores.save(p);
                });

        producto("Arroz extra 5 kg", "Bolsa de arroz para uso diario", "22.90", 35, abarrotes, proveedor);
        producto("Azúcar rubia 1 kg", "Azúcar rubia en bolsa", "4.50", 50, abarrotes, proveedor);
        producto("Aceite vegetal 1 L", "Aceite vegetal", "8.90", 24, abarrotes, proveedor);
        producto("Agua mineral  San Luis 625 ml", "Botella de agua", "2.00", 60, bebidas, proveedor);
        producto("Gaseosa de 1.5 L", "Gaseosa familiar", "7.50", 18, bebidas, proveedor);
        producto("Detergente  bolsa 800 g", "Detergente para ropa", "9.90", 4, limpieza, proveedor);

        if (clientes.findByDocumento("00000001").isEmpty()) {
            Cliente c = new Cliente();
            c.setNombres("Cliente");
            c.setApellidos("Demostración");
            c.setDocumento("00000001");
            c.setTelefono("999888777");
            c.setCorreo("cliente.registro@tiendaintegral.local");
            c.setEstado(Estado.ACTIVO);
            clientes.save(c);
        }
    }

    private void crearUsuario(String username, String correo, String password, Rol rol) {
        Usuario u = usuarios.findByUsername(username).orElseGet(Usuario::new);
        if (u.getId() == null) {
            u.setUsername(username);
            u.setCorreo(correo);
            u.setPasswordHash(encoder.encode(password));
            u.setRol(rol);
            u.setEstado(Estado.ACTIVO);
            usuarios.save(u);
        }
    }

    private Categoria categoria(String nombre, String descripcion) {
        return categorias.findAll().stream().filter(c -> nombre.equalsIgnoreCase(c.getNombre()))
                .findFirst().orElseGet(() -> {
                    Categoria c = new Categoria();
                    c.setNombre(nombre);
                    c.setDescripcion(descripcion);
                    c.setEstado(Estado.ACTIVO);
                    return categorias.save(c);
                });
    }

    private void producto(String nombre, String descripcion, String precio, int stock,
                          Categoria categoria, Proveedor proveedor) {
        if (productos.findByNombreContainingIgnoreCase(nombre).stream()
                .anyMatch(p -> nombre.equalsIgnoreCase(p.getNombre()))) return;
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(new BigDecimal(precio));
        p.setStock(stock);
        p.setStockMinimo(5);
        p.setEstado(Estado.ACTIVO);
        p.setCategoria(categoria);
        p.setProveedor(proveedor);
        productos.save(p);
    }
}
