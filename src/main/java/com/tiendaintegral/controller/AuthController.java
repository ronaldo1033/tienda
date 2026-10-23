package com.tiendaintegral.controller;
import com.tiendaintegral.dto.request.*; import com.tiendaintegral.dto.response.AuthResponse; import com.tiendaintegral.service.AuthService;
import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
 private final AuthService service; public AuthController(AuthService s){service=s;}
 @PostMapping("/registro") @ResponseStatus(HttpStatus.CREATED) public AuthResponse register(@Valid @RequestBody RegistroRequest r){return service.register(r);}
 @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest r){return service.login(r);}
}
