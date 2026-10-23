package com.tiendaintegral.controller;
import com.tiendaintegral.model.Usuario; import com.tiendaintegral.repository.UsuarioRepository; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/usuarios") @PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {
 private final UsuarioRepository repo; public UsuarioController(UsuarioRepository r){repo=r;}
 public record UsuarioResponse(Long id,String username,String correo,String rol,String estado){}
 @GetMapping public List<UsuarioResponse> listar(){return repo.findAll().stream().map(u->new UsuarioResponse(u.getId(),u.getUsername(),u.getCorreo(),u.getRol().name(),u.getEstado().name())).toList();}
}
