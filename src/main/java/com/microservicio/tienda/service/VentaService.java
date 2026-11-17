package com.microservicio.tienda.service;

import com.microservicio.tienda.dto.DetalleVentaDTO;
import com.microservicio.tienda.dto.VentaRequestDTO;
import com.microservicio.tienda.dto.VentaResponseDTO;
import com.microservicio.tienda.exception.ResourceNotFoundException;
import com.microservicio.tienda.model.DetalleVenta;
import com.microservicio.tienda.model.Producto;
import com.microservicio.tienda.model.Venta;
import com.microservicio.tienda.repository.ProductoRepository;
import com.microservicio.tienda.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> findAll() {
        return ventaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaResponseDTO findById(Long id) {
        Venta venta = ventaRepository.findByIdWithDetalles(id);
        if (venta == null) {
            throw new ResourceNotFoundException("Venta no encontrada con id: " + id);
        }
        return toResponseDTO(venta);
    }

    @Transactional
    public VentaResponseDTO create(VentaRequestDTO requestDTO) {
        // Crear la venta
        Venta venta = Venta.builder()
                .clienteNombre(requestDTO.getClienteNombre())
                .clienteEmail(requestDTO.getClienteEmail())
                .clienteTelefono(requestDTO.getClienteTelefono())
                .fechaVenta(LocalDateTime.now())
                .estado(requestDTO.getEstado())
                .total(BigDecimal.ZERO)
                .build();

        // Calcular el total y crear los detalles
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaDTO detalleDTO : requestDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado con id: " + detalleDTO.getProductoId()));

            // Verificar stock disponible
            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: " + producto.getNombre() +
                                ". Stock disponible: " + producto.getStock());
            }

            // Crear detalle de venta
            BigDecimal precioUnitario = detalleDTO.getPrecioUnitario() != null ?
                    detalleDTO.getPrecioUnitario() : producto.getPrecio();

            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));

            DetalleVenta detalle = DetalleVenta.builder()
                    .producto(producto)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotal)
                    .build();

            venta.addDetalle(detalle);
            total = total.add(subtotal);

            // Actualizar stock del producto
            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepository.save(producto);
        }

        venta.setTotal(total);
        Venta savedVenta = ventaRepository.save(venta);

        return toResponseDTO(savedVenta);
    }

    @Transactional
    public VentaResponseDTO updateEstado(Long id, Venta.EstadoVenta nuevoEstado) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));

        // Si se cancela la venta, devolver el stock
        if (nuevoEstado == Venta.EstadoVenta.CANCELADA && venta.getEstado() != Venta.EstadoVenta.CANCELADA) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            }
        }

        venta.setEstado(nuevoEstado);
        Venta updatedVenta = ventaRepository.save(venta);

        return toResponseDTO(updatedVenta);
    }

    @Transactional
    public void delete(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));

        // Devolver el stock si la venta estaba completada
        if (venta.getEstado() == Venta.EstadoVenta.COMPLETADA) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            }
        }

        ventaRepository.delete(venta);
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> findByEstado(Venta.EstadoVenta estado) {
        return ventaRepository.findByEstado(estado).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> findByClienteNombre(String nombre) {
        return ventaRepository.findByClienteNombreContainingIgnoreCase(nombre).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Método de mapeo Entity -> DTO
    private VentaResponseDTO toResponseDTO(Venta venta) {
        List<DetalleVentaDTO> detallesDTO = venta.getDetalles().stream()
                .map(detalle -> DetalleVentaDTO.builder()
                        .id(detalle.getId())
                        .productoId(detalle.getProducto().getId())
                        .productoNombre(detalle.getProducto().getNombre())
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return VentaResponseDTO.builder()
                .id(venta.getId())
                .clienteNombre(venta.getClienteNombre())
                .clienteEmail(venta.getClienteEmail())
                .clienteTelefono(venta.getClienteTelefono())
                .fechaVenta(venta.getFechaVenta())
                .total(venta.getTotal())
                .estado(venta.getEstado())
                .detalles(detallesDTO)
                .build();
    }
}
