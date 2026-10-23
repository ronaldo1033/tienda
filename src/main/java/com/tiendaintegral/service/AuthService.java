package com.tiendaintegral.service;
import com.tiendaintegral.dto.request.*; import com.tiendaintegral.dto.response.AuthResponse; import com.tiendaintegral.exception.BusinessException;
import com.tiendaintegral.model.*; import com.tiendaintegral.repository.UsuarioRepository; import com.tiendaintegral.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service
public class AuthService {
 private final UsuarioRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UsuarioRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 @Transactional public AuthResponse register(RegistroRequest r){if(users.existsByUsername(r.username())||users.existsByCorreo(r.correo()))throw new BusinessException("El usuario o correo ya está registrado.");
  Usuario u=new Usuario();u.setUsername(r.username());u.setCorreo(r.correo());u.setPasswordHash(encoder.encode(r.password()));u.setRol(Rol.CLIENTE);u.setEstado(Estado.ACTIVO);users.save(u);return auth(u);}
 public AuthResponse login(LoginRequest r){Usuario u=users.findByUsername(r.username()).filter(x->encoder.matches(r.password(),x.getPasswordHash())&&x.getEstado()==Estado.ACTIVO).orElseThrow(()->new BusinessException("Credenciales inválidas."));return auth(u);}
 private AuthResponse auth(Usuario u){return new AuthResponse(jwt.generate(u.getUsername(),u.getRol().name()),"Bearer",u.getId(),u.getUsername(),u.getRol().name());}
}
