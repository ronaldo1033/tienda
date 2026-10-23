package com.tiendaintegral.dto.request;
import jakarta.validation.constraints.*;
public record RegistroRequest(@NotBlank String username,@Email @NotBlank String correo,@Size(min=8) String password) {}
