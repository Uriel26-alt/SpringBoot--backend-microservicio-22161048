package com.microservicio.tienda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.tienda.controller.ProductoController;
import com.microservicio.tienda.dto.ProductoRequestDTO;
import com.microservicio.tienda.dto.ProductoResponseDTO;
import com.microservicio.tienda.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    @Test
    @WithMockUser
    void testGetAllProductos_ReturnsOk() throws Exception {
        // Arrange
        ProductoResponseDTO producto1 = ProductoResponseDTO.builder()
                .id(1L)
                .nombre("Laptop HP")
                .precio(new BigDecimal("12500.00"))
                .stock(10)
                .activo(true)
                .build();

        List<ProductoResponseDTO> productos = Arrays.asList(producto1);
        when(productoService.findAll()).thenReturn(productos);

        // Act & Assert
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Laptop HP"))
                .andExpect(jsonPath("$[0].precio").value(12500.00));
    }

    @Test
    @WithMockUser
    void testGetProductoById_ExistingId_ReturnsOk() throws Exception {
        // Arrange
        ProductoResponseDTO producto = ProductoResponseDTO.builder()
                .id(1L)
                .nombre("Laptop HP")
                .precio(new BigDecimal("12500.00"))
                .stock(10)
                .activo(true)
                .build();

        when(productoService.findById(1L)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop HP"));
    }

    @Test
    @WithMockUser
    void testCreateProducto_ValidData_ReturnsCreated() throws Exception {
        // Arrange
        ProductoRequestDTO requestDTO = ProductoRequestDTO.builder()
                .nombre("Laptop HP")
                .descripcion("Laptop HP Pavilion")
                .precio(new BigDecimal("12500.00"))
                .stock(10)
                .activo(true)
                .build();

        ProductoResponseDTO responseDTO = ProductoResponseDTO.builder()
                .id(1L)
                .nombre("Laptop HP")
                .descripcion("Laptop HP Pavilion")
                .precio(new BigDecimal("12500.00"))
                .stock(10)
                .activo(true)
                .build();

        when(productoService.create(any(ProductoRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop HP"));
    }

    @Test
    @WithMockUser
    void testUpdateProducto_ValidData_ReturnsOk() throws Exception {
        // Arrange
        ProductoRequestDTO requestDTO = ProductoRequestDTO.builder()
                .nombre("Laptop HP Updated")
                .descripcion("Nueva descripción")
                .precio(new BigDecimal("13000.00"))
                .stock(15)
                .activo(true)
                .build();

        ProductoResponseDTO responseDTO = ProductoResponseDTO.builder()
                .id(1L)
                .nombre("Laptop HP Updated")
                .descripcion("Nueva descripción")
                .precio(new BigDecimal("13000.00"))
                .stock(15)
                .activo(true)
                .build();

        when(productoService.update(eq(1L), any(ProductoRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/productos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop HP Updated"))
                .andExpect(jsonPath("$.precio").value(13000.00));
    }

    @Test
    @WithMockUser
    void testDeleteProducto_ExistingId_ReturnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/productos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
