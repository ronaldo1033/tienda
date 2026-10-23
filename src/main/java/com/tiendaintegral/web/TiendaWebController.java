package com.tiendaintegral.web;

import com.tiendaintegral.dto.request.LoginRequest;
import com.tiendaintegral.dto.request.RegistroRequest;
import com.tiendaintegral.model.Categoria;
import com.tiendaintegral.model.Estado;
import com.tiendaintegral.model.Producto;
import com.tiendaintegral.service.AuthService;
import com.tiendaintegral.service.CatalogoService;
import com.tiendaintegral.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

    public TiendaWebController(CatalogoService catalogo, AuthService auth, ProductoRepository productos) {
        this.catalogo = catalogo; this.auth = auth; this.productos = productos;
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
        return "tienda/index";
    }

    @GetMapping("/tienda/producto/{id}")
    public String detalle(@PathVariable Long id, Model model, HttpSession session) {
        Producto p = catalogo.producto(id);
        if (p.getEstado() != Estado.ACTIVO) return "redirect:/";
        model.addAttribute("producto", p);
        model.addAttribute("usuario", session.getAttribute("usuario"));
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
                                   @RequestParam String confirmarPassword, RedirectAttributes flash) {
        if (password == null || password.length() < 8) {
            flash.addFlashAttribute("error", "La contraseña debe tener al menos 8 caracteres."); return "redirect:/registro";
        }
        if (!password.equals(confirmarPassword)) {
            flash.addFlashAttribute("error", "Las contraseñas no coinciden."); return "redirect:/registro";
        }
        try {
            auth.register(new RegistroRequest(username, correo, password));
            flash.addFlashAttribute("mensaje", "Cuenta creada. Ya puedes iniciar sesión."); return "redirect:/login";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "No se pudo crear la cuenta. Verifica que el usuario y correo no estén registrados."); return "redirect:/registro";
        }
    }

    @GetMapping("/logout") public String logout(HttpSession session) { session.invalidate(); return "redirect:/"; }

    @GetMapping("/tienda/admin")
    public String admin(Model model, HttpSession session, RedirectAttributes flash) {
        if (!esAdmin(session)) { flash.addFlashAttribute("error", "Inicia sesión con una cuenta ADMIN para entrar al panel."); return "redirect:/login"; }
        model.addAttribute("productos", catalogo.productos());
        model.addAttribute("categorias", catalogo.categorias());
        model.addAttribute("stockBajo", catalogo.stockBajo().size());
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

    private boolean esAdmin(HttpSession s) { return "ADMIN".equals(s.getAttribute("rol")); }
}
