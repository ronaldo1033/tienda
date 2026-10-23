package com.tiendaintegral.dto.request;
import jakarta.validation.constraints.*;
public record MovimientoRequest(@NotNull Long productoId,@NotNull Tipo tipo,@Min(1) int cantidad,@NotBlank String motivo) {
 public enum Tipo { ENTRADA, SALIDA }
}
