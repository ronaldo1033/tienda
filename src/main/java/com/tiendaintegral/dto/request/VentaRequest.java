package com.tiendaintegral.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*; import java.util.List;
public record VentaRequest(@NotNull Long clienteId,Long direccionId,@NotEmpty List<@Valid Item> items) {
 public record Item(@NotNull Long productoId,@Min(1) int cantidad) {}
}
