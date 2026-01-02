package Product_Service.Service;

import Product_Service.Exception.ProductAlreadyExistByProductSN;
import Product_Service.Exception.ProductNotFoundException;
import Product_Service.Kafka.ProductEventPublisher;
import Product_Service.Mapper.ProductMapper;
import Product_Service.Model.Product;
import Product_Service.Outbox.OutboxEvent;
import Product_Service.Outbox.OutboxRepo;
import Product_Service.ProductDTO.ProductRequestDTO;
import Product_Service.ProductDTO.ProductResponseDTO;
import Product_Service.ProductRepository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductEventPublisher productEventPublisher;
    private final OutboxRepo outboxRepo;
    private final ObjectMapper objectMapper; // Add Jackson's ObjectMapper

    public ProductService(ProductRepository productRepository, ProductEventPublisher productEventPublisher, OutboxRepo outboxRepo, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.productEventPublisher = productEventPublisher;
        this.outboxRepo = outboxRepo;
        this.objectMapper = objectMapper;
    }


    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        log.info("Creating new product with SN: {}", productRequestDTO.getProductSN());

        if (productRepository.existsByProductSN(productRequestDTO.getProductSN())) {
            log.warn("Product creation failed: Product with SN {} already exists", productRequestDTO.getProductSN());
            throw new ProductAlreadyExistByProductSN("Product already exists with SN: " + productRequestDTO.getProductSN());
        }

        try {
            // Create and save the product
            Product newProduct = ProductMapper.toProductModel(productRequestDTO);
            Product savedProduct = productRepository.save(newProduct);
            log.info("Successfully saved product with ID: {}", savedProduct.getProductId());

            try {
                // Debug log before creating the event
                log.info("Creating OutboxEvent for product ID: {}", savedProduct.getProductId());

                // Create and save the initial outbox event
                OutboxEvent event = OutboxEvent.builder()
                    .aggregateType("Product")
                    .aggregateId(savedProduct.getProductId())
                    .eventType("ProductCreated")
                    .eventPayload(convertToJSON(savedProduct))
                    .eventMetadata("Product created successfully")
                    .build();

                // Debug log after creating the event
                log.info("Created OutboxEvent - aggregateType: {}, aggregateId: {}",
                    event.getAggregateType(), event.getAggregateId());

                log.info("About to save OutboxEvent to database");
                outboxRepo.saveAndFlush(event);  // Ensure it's written immediately
                log.info("Successfully saved OutboxEvent to database");
                log.debug("Saved outbox event for product ID: {}", savedProduct.getProductId());

                // Publish the event
                productEventPublisher.productEventCreated(savedProduct);
                log.info("Published product created event for product ID: {}", savedProduct.getProductId());

                // Update the event status
                event.setEventStatus("PUBLISHED");
                outboxRepo.saveAndFlush(event);

                return ProductMapper.responseDTO(savedProduct);

            } catch (Exception e) {
                log.error("Failed to process outbox event for product ID: {}", savedProduct.getProductId(), e);
                // The product was saved but event processing failed
                // Consider adding a retry mechanism or dead letter queue here
                throw new RuntimeException("Failed to process product event", e);
            }
        } catch (Exception e) {
            log.error("Error occurred while creating product: {}", e.getMessage(), e);
            throw e;
        }
    }

    public String convertToJSON(Product product){
        try {
            return objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            log.error("Error converting product to JSON", e);
            throw new RuntimeException("Failed to convert product to JSON", e);
        }
    }

    public List<ProductResponseDTO> getAllProduct() {
        List<Product> productList = productRepository.findAll();

        List<ProductResponseDTO> responseDTOS;
        responseDTOS = productList.stream().map(
                ProductMapper::responseDTO).toList();
        return responseDTOS;
    }

    public ProductResponseDTO findProductById(UUID id) {

    Product product = productRepository.findById(id).orElseThrow(()
            -> new ProductNotFoundException("Product not found"));

    return  ProductMapper.responseDTO(product);
    }

}