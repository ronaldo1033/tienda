package com.tiendaintegral.web;

import com.tiendaintegral.dto.request.LoginRequest;
import com.tiendaintegral.dto.request.RegistroRequest;
import com.tiendaintegral.model.Categoria;
import com.tiendaintegral.model.Estado;
import com.tiendaintegral.model.Producto;
import com.tiendaintegral.service.AuthService;
import com.tiendaintegral.service.CatalogoService;
import com.tiendaintegral.repository.ProductoRepository;
import com.tiendaintegral.repository.VentaRepository;
import com.tiendaintegral.repository.UsuarioRepository;
import com.tiendaintegral.repository.ProveedorRepository;
import com.tiendaintegral.repository.PagoRepository;
import com.tiendaintegral.model.EstadoPago;
import com.tiendaintegral.model.Venta;
import com.tiendaintegral.model.Proveedor;
import com.tiendaintegral.model.Usuario;
import com.tiendaintegral.model.Cliente;
import com.tiendaintegral.model.DireccionEnvio;
import com.tiendaintegral.model.MetodoPago;
import com.tiendaintegral.dto.request.VentaRequest;
import com.tiendaintegral.service.VentaService;
import com.tiendaintegral.service.PagoService;
import com.tiendaintegral.repository.ClienteRepository;
import com.tiendaintegral.repository.DireccionEnvioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
public class TiendaWebController {
    private final CatalogoService catalogo;
    private final AuthService auth;
    private final ProductoRepository productos;
    private final VentaRepository ventas;
    private final UsuarioRepository usuarios;
    private final ProveedorRepository proveedores;
    private final PagoRepository pagos;
    private final ClienteRepository clientes;
    private final DireccionEnvioRepository direcciones;
    private final VentaService ventaService;
    private final PagoService pagoService;

    public TiendaWebController(CatalogoService catalogo, AuthService auth, ProductoRepository productos,
                               VentaRepository ventas, UsuarioRepository usuarios,
                               ProveedorRepository proveedores, PagoRepository pagos, ClienteRepository clientes,
                               DireccionEnvioRepository direcciones, VentaService ventaService, PagoService pagoService) {
        this.catalogo = catalogo; this.auth = auth; this.productos = productos;
        this.ventas = ventas; this.usuarios = usuarios; this.proveedores = proveedores; this.pagos = pagos;
        this.clientes = clientes; this.direcciones = direcciones; this.ventaService = ventaService; this.pagoService = pagoService;
    }

    @GetMapping({"/", "/tienda"})
    public String inicio(@RequestParam(required = false) String q, Model model, HttpSession session) {
        var lista = catalogo.productos().stream()
                .filter(p -> p.getEstado() == Estado.ACTIVO)
                .filter(p -> q == null || q.isBlank() || p.getNombre().toLowerCase().contains(q.toLowerCase()) || (p.getDescripcion() != null && p.getDescripcion().toLowerCase().contains(q.toLowerCase())))
                .toList();
        model.addAttribute("productos", lista);
        model.addAttribute("categorias", catalogo.categorias().stream().filter(c -> c.getEstado() == Estado.ACTIVO).toList());
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("cantidadCarrito", cantidadCarrito(session));
        return "tienda/index";
    }

    @GetMapping("/tienda/producto/{id}")
    public String detalle(@PathVariable Long id, Model model, HttpSession session) {
        Producto p = catalogo.producto(id);
        if (p.getEstado() != Estado.ACTIVO) return "redirect:/";
        model.addAttribute("producto", p);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("cantidadCarrito", cantidadCarrito(session));
        return "tienda/detalle";
    }

    @GetMapping("/login") public String login(Model model) { return "tienda/login"; }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes flash) {
        try {
            var r = auth.login(new LoginRequest(username, password));
            session.setAttribute("usuario", r.username()); session.setAttribute("rol", r.rol()); session.setAttribute("token", r.token());
            return "ADMIN".equals(r.rol()) ? "redirect:/tienda/admin" : "redirect:/";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "Usuario o contraseña incorrectos, o cuenta inactiva.");
            return "redirect:/login";
        }
    }

    @GetMapping("/registro") public String registro() { return "tienda/registro"; }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String username, @RequestParam String correo, @RequestParam String password,
                                   @RequestParam String confirmarPassword, @RequestParam String nombres,
                                   @RequestParam String apellidos, @RequestParam String documento,
                                   @RequestParam(required = false) String telefono, RedirectAttributes flash) {
        if (password == null || password.length() < 8) {
            flash.addFlashAttribute("error", "La contraseña debe tener al menos 8 caracteres."); return "redirect:/registro";
        }
        if (!password.equals(confirmarPassword)) {
            flash.addFlashAttribute("error", "Las contraseñas no coinciden."); return "redirect:/registro";
        }
        try {
            if (nombres.isBlank() || apellidos.isBlank() || documento.isBlank()) throw new IllegalArgumentException();
            String correoNormalizado = correo.trim().toLowerCase();
            if (clientes.findByCorreo(correoNormalizado).isPresent() || clientes.findByDocumento(documento.trim()).isPresent()) throw new IllegalArgumentException();
            auth.register(new RegistroRequest(username.trim(), correoNormalizado, password));
            Cliente cliente = new Cliente(); cliente.setNombres(nombres.trim()); cliente.setApellidos(apellidos.trim());
            cliente.setDocumento(documento.trim()); cliente.setTelefono(telefono == null ? null : telefono.trim());
            cliente.setCorreo(correoNormalizado); cliente.setEstado(Estado.ACTIVO); clientes.save(cliente);
            flash.addFlashAttribute("mensaje", "Cuenta creada correctamente. Ya puedes iniciar sesión y comprar."); return "redirect:/login";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se pudo crear la cuenta. Revisa tus datos y que el usuario, correo o documento no estén registrados."); return "redirect:/registro";
        }
    }

    @GetMapping("/logout") public String logout(HttpSession session) { session.invalidate(); return "redirect:/"; }

    @PostMapping("/carrito/agregar")
    public String agregarCarrito(@RequestParam Long productoId, @RequestParam(defaultValue = "1") int cantidad,
                                 HttpSession session, RedirectAttributes flash) {
        try {
            if (cantidad < 1 || cantidad > 99) throw new IllegalArgumentException();
            Producto producto = catalogo.producto(productoId);
            if (producto.getEstado() != Estado.ACTIVO || producto.getStock() < 1) throw new IllegalArgumentException();
            var carrito = obtenerCarrito(session);
            int nuevaCantidad = carrito.getOrDefault(productoId, 0) + cantidad;
            if (nuevaCantidad > producto.getStock()) throw new IllegalArgumentException();
            carrito.put(productoId, nuevaCantidad);
            session.setAttribute("carrito", carrito);
            flash.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        } catch (RuntimeException ex) { flash.addFlashAttribute("error", "No se pudo agregar: verifica el producto y el stock disponible."); }
        return "redirect:/carrito";
    }

    @GetMapping("/carrito")
    public String carrito(Model model, HttpSession session) {
        var lineas = lineasCarrito(session);
        model.addAttribute("lineas", lineas);
        model.addAttribute("total", lineas.stream().map(CartLine::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        model.addAttribute("cantidadCarrito", cantidadCarrito(session));
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "tienda/carrito";
    }

    @PostMapping("/carrito/actualizar")
    public String actualizarCarrito(@RequestParam Long productoId, @RequestParam int cantidad, HttpSession session, RedirectAttributes flash) {
        try {
            var carrito = obtenerCarrito(session);
            if (!carrito.containsKey(productoId)) throw new IllegalArgumentException();
            if (cantidad <= 0) carrito.remove(productoId);
            else {
                Producto p = catalogo.producto(productoId);
                if (cantidad > p.getStock()) throw new IllegalArgumentException();
                carrito.put(productoId, cantidad);
            }
            session.setAttribute("carrito", carrito);
        } catch (RuntimeException ex) { flash.addFlashAttribute("error", "La cantidad supera el stock disponible."); }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam Long productoId, HttpSession session) {
        var carrito = obtenerCarrito(session); carrito.remove(productoId); session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session, RedirectAttributes flash) {
        if (!esCliente(session)) return "redirect:/login";
        Cliente cliente = clienteSesion(session);
        if (cliente == null) { flash.addFlashAttribute("error", "Tu cuenta no tiene un perfil de cliente. Regístrate nuevamente con tus datos personales."); return "redirect:/"; }
        var lineas = lineasCarrito(session);
        if (lineas.isEmpty()) return "redirect:/carrito";
        model.addAttribute("cliente", cliente); model.addAttribute("lineas", lineas);
        model.addAttribute("total", lineas.stream().map(CartLine::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        model.addAttribute("cantidadCarrito", cantidadCarrito(session)); model.addAttribute("usuario", session.getAttribute("usuario"));
        return "tienda/checkout";
    }

    @PostMapping("/checkout")
    public String confirmarCompra(@RequestParam String direccion, @RequestParam String distrito,
                                  @RequestParam String ciudad, @RequestParam String departamento,
                                  @RequestParam(required = false) String referencia, @RequestParam MetodoPago metodo,
                                  HttpSession session, RedirectAttributes flash) {
        if (!esCliente(session)) return "redirect:/login";
        Cliente cliente = clienteSesion(session);
        var carrito = obtenerCarrito(session);
        if (cliente == null || carrito.isEmpty()) return "redirect:/carrito";
        try {
            DireccionEnvio envio = new DireccionEnvio(); envio.setCliente(cliente); envio.setDireccion(direccion.trim());
            envio.setDistrito(distrito.trim()); envio.setCiudad(ciudad.trim()); envio.setDepartamento(departamento.trim());
            envio.setReferencia(referencia == null ? null : referencia.trim()); envio.setPrincipal(true);
            envio = direcciones.save(envio);
            var items = carrito.entrySet().stream().map(e -> new VentaRequest.Item(e.getKey(), e.getValue())).toList();
            Venta venta = ventaService.crear(new VentaRequest(cliente.getId(), envio.getId(), items));
            pagoService.simular(venta.getId(), metodo, venta.getTotal(), true);
            session.setAttribute("carrito", new java.util.LinkedHashMap<Long, Integer>());
            flash.addFlashAttribute("mensaje", "¡Compra registrada! Tu pedido #" + venta.getId() + " fue confirmado. El pago es una simulación de demostración.");
            return "redirect:/mi-cuenta";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se pudo confirmar la compra: " + ex.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/mi-cuenta")
    public String miCuenta(Model model, HttpSession session, RedirectAttributes flash) {
        if (!esCliente(session)) return "redirect:/login";
        Cliente cliente = clienteSesion(session);
        if (cliente == null) { flash.addFlashAttribute("error", "No encontramos tu perfil de cliente."); return "redirect:/"; }
        model.addAttribute("cliente", cliente); model.addAttribute("compras", ventas.findByClienteId(cliente.getId()));
        model.addAttribute("cantidadCarrito", cantidadCarrito(session)); model.addAttribute("usuario", session.getAttribute("usuario"));
        return "tienda/mi-cuenta";
    }

    private boolean esCliente(HttpSession s) { return "CLIENTE".equals(s.getAttribute("rol")) && s.getAttribute("usuario") != null; }
    private Cliente clienteSesion(HttpSession s) {
        Object username = s.getAttribute("usuario");
        if (username == null) return null;
        return usuarios.findByUsername(username.toString()).flatMap(u -> clientes.findByCorreo(u.getCorreo())).orElse(null);
    }
    @SuppressWarnings("unchecked")
    private java.util.LinkedHashMap<Long, Integer> obtenerCarrito(HttpSession s) {
        Object actual = s.getAttribute("carrito");
        if (actual instanceof java.util.LinkedHashMap<?, ?>) return (java.util.LinkedHashMap<Long, Integer>) actual;
        var nuevo = new java.util.LinkedHashMap<Long, Integer>(); s.setAttribute("carrito", nuevo); return nuevo;
    }
    private int cantidadCarrito(HttpSession s) { return obtenerCarrito(s).values().stream().mapToInt(Integer::intValue).sum(); }
    private java.util.List<CartLine> lineasCarrito(HttpSession s) {
        var result = new java.util.ArrayList<CartLine>();
        for (var e : obtenerCarrito(s).entrySet()) {
            try { Producto p = catalogo.producto(e.getKey()); if (p.getEstado() == Estado.ACTIVO) result.add(new CartLine(p, e.getValue(), p.getPrecio().multiply(BigDecimal.valueOf(e.getValue())))); }
            catch (RuntimeException ignored) { }
        }
        return result;
    }
    public record CartLine(Producto producto, int cantidad, BigDecimal subtotal) { }

    @GetMapping("/tienda/admin")
    public String admin(Model model, HttpSession session, RedirectAttributes flash) {
        if (!esAdmin(session)) { flash.addFlashAttribute("error", "Inicia sesión con una cuenta ADMIN para entrar al panel."); return "redirect:/login"; }
        model.addAttribute("productos", catalogo.productos());
        model.addAttribute("categorias", catalogo.categorias());
        model.addAttribute("stockBajo", catalogo.stockBajo().size());
        model.addAttribute("ventas", ventas.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "fecha")));
        model.addAttribute("usuarios", usuarios.findAll());
        model.addAttribute("clientes", catalogo.clientes());
        model.addAttribute("proveedores", proveedores.findAll());
        model.addAttribute("ingresos", pagos.findAll().stream()
                .filter(p -> p.getEstado() == EstadoPago.APROBADO)
                .map(com.tiendaintegral.model.Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        model.addAttribute("totalVentas", ventas.count());
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "tienda/admin";
    }

    @PostMapping("/tienda/admin/productos")
    public String crearProducto(@RequestParam String nombre, @RequestParam(required=false) String descripcion,
                                @RequestParam BigDecimal precio, @RequestParam int stock, @RequestParam(defaultValue="5") int stockMinimo,
                                @RequestParam Long categoriaId, HttpSession session, RedirectAttributes flash) {
        if (!esAdmin(session)) return "redirect:/login";
        try {
            if (precio.compareTo(BigDecimal.ZERO) < 0 || stock < 0 || stockMinimo < 0) throw new IllegalArgumentException();
            Categoria categoria = catalogo.categoria(categoriaId);
            Producto p = new Producto(); p.setNombre(nombre.trim()); p.setDescripcion(descripcion); p.setPrecio(precio);
            p.setStock(stock); p.setStockMinimo(stockMinimo); p.setCategoria(categoria); p.setEstado(Estado.ACTIVO);
            productos.save(p);
            flash.addFlashAttribute("mensaje", "Producto agregado correctamente.");
        } catch (RuntimeException ex) { flash.addFlashAttribute("error", "No se pudo guardar. Revisa los datos y la categoría."); }
        return "redirect:/tienda/admin";
    }

    @PostMapping("/tienda/admin/categorias")
    public String crearCategoria(@RequestParam String nombre, @RequestParam(required = false) String descripcion,
                                 HttpSession session, RedirectAttributes flash) {
        if (!esAdmin(session)) return "redirect:/login";
        try {
            if (nombre == null || nombre.isBlank() || nombre.trim().length() > 100) {
                throw new IllegalArgumentException("Nombre inválido");
            }
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre.trim());
            categoria.setDescripcion(descripcion == null ? null : descripcion.trim());
            categoria.setEstado(Estado.ACTIVO);
            catalogo.guardarCategoria(categoria);
            flash.addFlashAttribute("mensaje", "Categoría creada correctamente.");
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se pudo crear la categoría. Verifica que el nombre no esté repetido.");
        }
        return "redirect:/tienda/admin#categorias";
    }

    private boolean esAdmin(HttpSession s) { return "ADMIN".equals(s.getAttribute("rol")); }
}
