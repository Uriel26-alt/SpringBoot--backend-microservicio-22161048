package com.microservicio.tienda.controller;

import com.microservicio.tienda.dto.ProductoRequestDTO;
import com.microservicio.tienda.dto.ProductoResponseDTO;
import com.microservicio.tienda.service.ProductoService;
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
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Listar todos los productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @Operation(summary = "Listar productos activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos activos obtenida exitosamente")
    })
    @GetMapping("/activos")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosActivos() {
        return ResponseEntity.ok(productoService.findAllActivos());
    }

    @Operation(summary = "Obtener producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getProductoById(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProducto(
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        ProductoResponseDTO createdProducto = productoService.create(requestDTO);
        return new ResponseEntity<>(createdProducto, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        return ResponseEntity.ok(productoService.update(id, requestDTO));
    }

    @Operation(summary = "Eliminar (desactivar) un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar productos por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> searchProductos(
            @Parameter(description = "Nombre o parte del nombre del producto")
            @RequestParam String nombre) {
        return ResponseEntity.ok(productoService.searchByNombre(nombre));
    }
}
