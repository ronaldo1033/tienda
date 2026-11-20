package com.tiendaintegral.config;
import com.tiendaintegral.security.JwtAuthFilter; import org.springframework.context.annotation.*; import org.springframework.security.authentication.*; import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.*; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration @EnableMethodSecurity
public class SecurityConfig {
 @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtAuthFilter jwt)throws Exception{
  return http.headers(h->h.frameOptions(f->f.sameOrigin())).csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a->a.requestMatchers("/","/tienda/**","/login","/registro","/logout","/carrito/**","/checkout","/mi-cuenta","/css/**","/js/**","/images/**","/error","/api/v1/auth/**","/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**","/actuator/health","/h2-console/**").permitAll()
   .requestMatchers("/api/v1/reportes/**").hasRole("ADMIN").requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN")
   .requestMatchers("/api/v1/categorias/**","/api/v1/proveedores/**","/api/v1/productos/**","/api/v1/inventario/**").hasAnyRole("ADMIN","CLIENTE")
   .anyRequest().authenticated()).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build();
 }
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration c)throws Exception{return c.getAuthenticationManager();}
}
