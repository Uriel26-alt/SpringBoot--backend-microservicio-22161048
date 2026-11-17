package com.microservicio.tienda.dto;

import com.microservicio.tienda.model.Venta;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaResponseDTO {

    private Long id;
    private String clienteNombre;
    private String clienteEmail;
    private String clienteTelefono;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private Venta.EstadoVenta estado;
    private List<DetalleVentaDTO> detalles;
}
