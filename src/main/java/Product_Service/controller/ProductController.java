package Product_Service.controller;

import Product_Service.ProductDTO.ProductRequestDTO;
import Product_Service.ProductDTO.ProductResponseDTO;
import Product_Service.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
@Tag(name = "Product", description = "API for managing Product")
public class ProductController {

    private ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("/products")
    @Operation(summary = "create product")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO productResponseDTO = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @GetMapping("/products")
    @Operation(summary = "Get all products")
    public  ResponseEntity<List<ProductResponseDTO>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by id")
    public ResponseEntity<ProductResponseDTO> findProduct(@PathVariable UUID productId) {
        return new ResponseEntity<>(productService.findProductById(productId), HttpStatus.OK);
    }

}
