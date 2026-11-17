package com.microservicio.tienda.service;

import com.microservicio.tienda.dto.ProductoRequestDTO;
import com.microservicio.tienda.dto.ProductoResponseDTO;
import com.microservicio.tienda.exception.ResourceNotFoundException;
import com.microservicio.tienda.model.Producto;
import com.microservicio.tienda.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findAllActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return toResponseDTO(producto);
    }

    @Transactional
    public ProductoResponseDTO create(ProductoRequestDTO requestDTO) {
        if (productoRepository.existsByNombreIgnoreCase(requestDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe un producto con el nombre: " + requestDTO.getNombre());
        }

        Producto producto = toEntity(requestDTO);
        Producto savedProducto = productoRepository.save(producto);
        return toResponseDTO(savedProducto);
    }

    @Transactional
    public ProductoResponseDTO update(Long id, ProductoRequestDTO requestDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // Verificar si el nuevo nombre ya existe en otro producto
        if (!producto.getNombre().equalsIgnoreCase(requestDTO.getNombre()) &&
                productoRepository.existsByNombreIgnoreCase(requestDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe un producto con el nombre: " + requestDTO.getNombre());
        }

        producto.setNombre(requestDTO.getNombre());
        producto.setDescripcion(requestDTO.getDescripcion());
        producto.setPrecio(requestDTO.getPrecio());
        producto.setStock(requestDTO.getStock());
        producto.setActivo(requestDTO.getActivo());

        Producto updatedProducto = productoRepository.save(producto);
        return toResponseDTO(updatedProducto);
    }

    @Transactional
    public void delete(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // Soft delete: marcar como inactivo en lugar de eliminar
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> searchByNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Métodos de mapeo DTO <-> Entity
    private ProductoResponseDTO toResponseDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .activo(producto.getActivo())
                .build();
    }

    private Producto toEntity(ProductoRequestDTO requestDTO) {
        return Producto.builder()
                .nombre(requestDTO.getNombre())
                .descripcion(requestDTO.getDescripcion())
                .precio(requestDTO.getPrecio())
                .stock(requestDTO.getStock())
                .activo(requestDTO.getActivo() != null ? requestDTO.getActivo() : true)
                .build();
    }
}
