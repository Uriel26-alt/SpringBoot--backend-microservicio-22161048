package com.microservicio.tienda.dto;

import com.microservicio.tienda.model.Venta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre del cliente no puede exceder 100 caracteres")
    private String clienteNombre;

    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String clienteEmail;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String clienteTelefono;

    private Venta.EstadoVenta estado = Venta.EstadoVenta.COMPLETADA;

    @NotEmpty(message = "Debe incluir al menos un producto en la venta")
    @Valid
    private List<DetalleVentaDTO> detalles;
}
