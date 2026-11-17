package com.microservicio.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre del cliente no puede exceder 100 caracteres")
    @Column(name = "cliente_nombre", nullable = false, length = 100)
    private String clienteNombre;

    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(name = "cliente_email", length = 100)
    private String clienteEmail;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "cliente_telefono", length = 20)
    private String clienteTelefono;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoVenta estado = EstadoVenta.COMPLETADA;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleVenta> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaVenta == null) {
            fechaVenta = LocalDateTime.now();
        }
    }

    // Método helper para agregar detalles
    public void addDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }

    // Método helper para remover detalles
    public void removeDetalle(DetalleVenta detalle) {
        detalles.remove(detalle);
        detalle.setVenta(null);
    }

    public enum EstadoVenta {
        PENDIENTE,
        COMPLETADA,
        CANCELADA
    }
}
