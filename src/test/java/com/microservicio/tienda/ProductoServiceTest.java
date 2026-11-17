package com.microservicio.tienda;

import com.microservicio.tienda.dto.ProductoRequestDTO;
import com.microservicio.tienda.dto.ProductoResponseDTO;
import com.microservicio.tienda.exception.ResourceNotFoundException;
import com.microservicio.tienda.model.Producto;
import com.microservicio.tienda.repository.ProductoRepository;
import com.microservicio.tienda.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Laptop HP")
                .descripcion("Laptop HP Pavilion")
                .precio(new BigDecimal("12500.00"))
                .stock(10)
                .activo(true)
                .build();

        requestDTO = ProductoRequestDTO.builder()
                .nombre("Laptop HP")
                .descripcion("Laptop HP Pavilion")
                .precio(new BigDecimal("12500.00"))
                .stock(10)
                .activo(true)
                .build();
    }

    @Test
    void testFindAll_ReturnsProductList() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));

        // Act
        List<ProductoResponseDTO> result = productoService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop HP", result.get(0).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ExistingId_ReturnsProducto() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Act
        ProductoResponseDTO result = productoService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop HP", result.getNombre());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NonExistingId_ThrowsException() {
        // Arrange
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productoService.findById(999L);
        });
        verify(productoRepository, times(1)).findById(999L);
    }

    @Test
    void testCreate_ValidProducto_ReturnsCreatedProducto() {
        // Arrange
        when(productoRepository.existsByNombreIgnoreCase(anyString())).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act
        ProductoResponseDTO result = productoService.create(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Laptop HP", result.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testCreate_DuplicateName_ThrowsException() {
        // Arrange
        when(productoRepository.existsByNombreIgnoreCase("Laptop HP")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productoService.create(requestDTO);
        });
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testUpdate_ExistingProducto_ReturnsUpdatedProducto() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.existsByNombreIgnoreCase(anyString())).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoRequestDTO updateDTO = ProductoRequestDTO.builder()
                .nombre("Laptop HP Updated")
                .descripcion("Nueva descripción")
                .precio(new BigDecimal("13000.00"))
                .stock(15)
                .activo(true)
                .build();

        // Act
        ProductoResponseDTO result = productoService.update(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testDelete_ExistingProducto_MarksAsInactive() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act
        productoService.delete(1L);

        // Assert
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testSearchByNombre_ReturnsMatchingProducts() {
        // Arrange
        when(productoRepository.findByNombreContainingIgnoreCase("HP"))
                .thenReturn(Arrays.asList(producto));

        // Act
        List<ProductoResponseDTO> result = productoService.searchByNombre("HP");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getNombre().contains("HP"));
        verify(productoRepository, times(1)).findByNombreContainingIgnoreCase("HP");
    }
}
