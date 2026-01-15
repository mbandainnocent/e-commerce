package Product_Service.Service;

import Product_Service.Kafka.ProductEventPublisher;
import Product_Service.Model.Product;
import Product_Service.Outbox.OutboxEvent;
import Product_Service.Outbox.OutboxRepo;
import Product_Service.ProductDTO.ProductRequestDTO;
import Product_Service.ProductDTO.ProductResponseDTO;
import Product_Service.ProductRepository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OutboxRepo outboxRepo;
    @Mock
    private ProductEventPublisher productEventPublisher;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductService productService;
    
    private Product savedProduct;
    private ProductRequestDTO productRequest;
    
    @BeforeEach
    void setUp() {
        // Initialize test data
        productRequest = new ProductRequestDTO().builder()
                .productSN("SN-123")
                .productName("Iphone18")
                .productCategory("Electronics")
                .productDescription("Nice")
                .productPrice(BigDecimal.valueOf(200.50))
                .productManufacture("Apple")
                .build();

        savedProduct = new Product();
        savedProduct.setProductId(UUID.randomUUID());
        savedProduct.setProductSN("SN-123");
        savedProduct.setProductName("Iphone18");
        savedProduct.setProductCategory("Electronics");
        savedProduct.setProductDescription("Nice");
        savedProduct.setProductPrice(BigDecimal.valueOf(200.50));
        savedProduct.setProductManufacture("Apple");
    }


    @Test
    void testCreateProductIsSuccessful() throws JsonProcessingException {
        // Set up mocks
        when(productRepository.existsByProductSN("SN-123"))
                .thenReturn(false);

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        when(outboxRepo.saveAndFlush(any(OutboxEvent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
                
        // Mock ObjectMapper to return a JSON string when writeValueAsString is called with any Product
        when(objectMapper.writeValueAsString(any(Product.class)))
            .thenReturn(String.format("""
                {
                    "productId": "%s",
                    "productSN": "SN-123",
                    "productName": "Iphone18",
                    "productCategory": "Electronics",
                    "productDescription": "Nice",
                    "productPrice": 200.50,
                    "productManufacture": "Apple"
                }
                """, savedProduct.getProductId()));

        // Call the method under test
        ProductResponseDTO responseDTO = productService.createProduct(productRequest);

        // Assertions
        assertNotNull(responseDTO);
        assertEquals(savedProduct.getProductId(), responseDTO.getProductId());
        assertEquals(savedProduct.getProductName(), responseDTO.getProductName());

        // Verify interactions
        verify(productRepository, times(1)).existsByProductSN("SN-123");
        verify(productRepository, times(1)).save(any(Product.class));
        // Verify saveAndFlush is called twice: once for initial save and once for status update
        verify(outboxRepo, times(2)).saveAndFlush(any(OutboxEvent.class));
        verify(productEventPublisher, times(1)).productEventCreated(any(Product.class));
        
        // Verify ObjectMapper was called with the saved product
        verify(objectMapper, times(1)).writeValueAsString(savedProduct);

    }

    @Test
    void test_getAllProductIsSuccessful(){
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(),"IPhone","apple",
                        "Electronics","grt-product",
                        BigDecimal.valueOf(999.0),"SN-123"),
                new Product(UUID.randomUUID(), "SN-001",
                        "iPhone 15", "Electronics",
                        "Latest iPhone",
                        BigDecimal.valueOf(999.99), "Apple"),
                new Product(UUID.randomUUID(), "SN-002",
                        "Galaxy S23", "Electronics",
                        "Latest Samsung",
                        BigDecimal.valueOf(899.99), "Samsung"));

        when(productRepository.findAll()).thenReturn(products);

        //act
        List<ProductResponseDTO> findAllProducts = productService.getAllProduct();

        //verify the mapping is correct for each product
        ProductResponseDTO firstProduct = findAllProducts.get(0);
        assertEquals("IPhone", firstProduct.getProductName());
        assertEquals("apple", firstProduct.getProductManufacture());
        assertEquals("Electronics", firstProduct.getProductCategory());
    }

    @Test
    void test_findProductByIdIsSuccessful(){

        UUID productId = UUID.randomUUID();

        Product expectedProduct = new Product(
                productId,"IPhone","apple",
                "Electronics","grt-product",
                BigDecimal.valueOf(999.0),"SN-123");
        when(productRepository.findById(productId)). thenReturn(Optional.of(expectedProduct));



        ProductResponseDTO responseDTO = productService.findProductById(productId);

        //asserts

        assertNotNull(responseDTO, "Response DTO should not be null");
        assertEquals("IPhone", responseDTO.getProductName());
        assertEquals(expectedProduct.getProductName(), responseDTO.getProductName());
        assertEquals(expectedProduct.getProductManufacture(), responseDTO.getProductManufacture());
        assertEquals(expectedProduct.getProductCategory(), responseDTO.getProductCategory());
        assertEquals(expectedProduct.getProductDescription(), responseDTO.getProductDescription());
        assertEquals(expectedProduct.getProductPrice(), responseDTO.getProductPrice());
        assertEquals(expectedProduct.getProductSN(), responseDTO.getProductSN());

        verify(productRepository,times(1)).findById(productId);




    }

}