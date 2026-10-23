package com.tiendaintegral.security;
import com.tiendaintegral.repository.UsuarioRepository; import jakarta.servlet.*; import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException; import java.util.List;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 private final JwtService jwt; private final UsuarioRepository users;
 public JwtAuthFilter(JwtService jwt,UsuarioRepository users){this.jwt=jwt;this.users=users;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
  String h=req.getHeader("Authorization");
  if(h!=null&&h.startsWith("Bearer ")){String token=h.substring(7);if(jwt.valid(token)){String username=jwt.username(token);users.findByUsername(username).filter(u->u.getEstado().name().equals("ACTIVO")).ifPresent(u->{var auth=new UsernamePasswordAuthenticationToken(username,null,List.of(new SimpleGrantedAuthority("ROLE_"+u.getRol().name())));SecurityContextHolder.getContext().setAuthentication(auth);});}}
  chain.doFilter(req,res);
 }
}
