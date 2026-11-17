package com.microservicio.tienda.controller;

import com.microservicio.tienda.dto.VentaRequestDTO;
import com.microservicio.tienda.dto.VentaResponseDTO;
import com.microservicio.tienda.model.Venta;
import com.microservicio.tienda.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "API para gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    @Operation(summary = "Listar todas las ventas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> getAllVentas() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    @Operation(summary = "Obtener venta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> getVentaById(
            @Parameter(description = "ID de la venta") @PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    @Operation(summary = "Crear una nueva venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente")
    })
    @PostMapping
    public ResponseEntity<VentaResponseDTO> createVenta(
            @Valid @RequestBody VentaRequestDTO requestDTO) {
        VentaResponseDTO createdVenta = ventaService.create(requestDTO);
        return new ResponseEntity<>(createdVenta, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar estado de una venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de venta actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<VentaResponseDTO> updateEstadoVenta(
            @Parameter(description = "ID de la venta") @PathVariable Long id,
            @Parameter(description = "Nuevo estado de la venta") @RequestParam Venta.EstadoVenta estado) {
        return ResponseEntity.ok(ventaService.updateEstado(id, estado));
    }

    @Operation(summary = "Eliminar una venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(
            @Parameter(description = "ID de la venta") @PathVariable Long id) {
        ventaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar ventas por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VentaResponseDTO>> getVentasByEstado(
            @Parameter(description = "Estado de la venta") @PathVariable Venta.EstadoVenta estado) {
        return ResponseEntity.ok(ventaService.findByEstado(estado));
    }

    @Operation(summary = "Buscar ventas por nombre de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    })
    @GetMapping("/cliente")
    public ResponseEntity<List<VentaResponseDTO>> getVentasByCliente(
            @Parameter(description = "Nombre o parte del nombre del cliente")
            @RequestParam String nombre) {
        return ResponseEntity.ok(ventaService.findByClienteNombre(nombre));
    }
}
