package Product_Service.controller;

import Product_Service.Model.Product;
import Product_Service.ProductDTO.ProductRequestDTO;
import Product_Service.ProductDTO.ProductResponseDTO;
import Product_Service.Service.ProductService;
import Product_Service.SpringSecurity.SpringSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
@Import(SpringSecurity.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductController productController;

    @Test
    void shouldCreateProductSuccessfully() throws Exception {

        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setProductSN("SN-123");
        requestDTO.setProductName("Test Product");
        requestDTO.setProductCategory("Electronics");
        requestDTO.setProductDescription("Test Description");
        requestDTO.setProductPrice(BigDecimal.valueOf(999.99));
        requestDTO.setProductManufacture("Test Manufacturer");

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setProductId(UUID.randomUUID());
        responseDTO.setProductSN("SN-123");
        responseDTO.setProductName("Test Product");
        responseDTO.setProductCategory("Electronics");
        responseDTO.setProductDescription("Test Description");
        responseDTO.setProductPrice(BigDecimal.valueOf(999.99));
        responseDTO.setProductManufacture("Test Manufacturer");

        when(productService.createProduct(any(ProductRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.productPrice").value(999.99));

        verify(productService, times(1)).createProduct(any(ProductRequestDTO.class));
    }

    @Test
    void test_findAllProductIsSuccessful() throws Exception{
        UUID productId = UUID.randomUUID();

        List<ProductResponseDTO> responseDTOListList = Arrays.asList(
                new ProductResponseDTO(productId,"IPhone","apple",
                        "Electronics","grt-product",
                        BigDecimal.valueOf(999.0),"SN-123"),
                new ProductResponseDTO(UUID.randomUUID(), "iPhone 15",
                        "iPhone 15", "Electronics",
                        "Latest iPhone",
                        BigDecimal.valueOf(999.99), "Apple"),
                new ProductResponseDTO(UUID.randomUUID(), "Galaxy S23",
                        "Galaxy S23", "Electronics",
                        "Latest Samsung",
                        BigDecimal.valueOf(899.99), "Samsung"));
        when(productService.getAllProduct()).thenReturn(responseDTOListList);
        mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].productName", is("IPhone")))
                .andExpect(jsonPath("$[1].productName", is("iPhone 15")))
                .andExpect(jsonPath("$[2].productName", is("Galaxy S23")));

        verify(productService, times(1)).getAllProduct();

    }
}