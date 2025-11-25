package Product_Service.Service;

import Product_Service.Exception.ProductAlreadyExistByProductSN;
import Product_Service.Exception.ProductNotFoundException;
import Product_Service.Mapper.ProductMapper;
import Product_Service.Model.Product;
import Product_Service.ProductDTO.ProductRequestDTO;
import Product_Service.ProductDTO.ProductResponseDTO;
import Product_Service.ProductRepository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        if (productRepository.existsByProductSN(productRequestDTO.getProductSN())) {
            throw new ProductAlreadyExistByProductSN("Product already exist");
        }
        Product newproduct = productRepository.save(ProductMapper.toProductModel(productRequestDTO));

        return ProductMapper.responseDTO(newproduct);

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