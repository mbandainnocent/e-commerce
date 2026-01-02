package Product_Service.Mapper;

import Product_Service.Model.Product;
import Product_Service.ProductDTO.ProductRequestDTO;
import Product_Service.ProductDTO.ProductResponseDTO;

public class ProductMapper {
    public static ProductResponseDTO responseDTO (Product product){

        return ProductResponseDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productCategory(product.getProductCategory())
                .productDescription(product.getProductDescription())
                .productManufacture(product.getProductManufacture())
                .productPrice(product.getProductPrice())
                .productSN(product.getProductSN())
                .build();

    }

    public static Product toProductModel (ProductRequestDTO productRequestDTO){

        return Product.builder()
                .productName(productRequestDTO.getProductName())
                .productManufacture(productRequestDTO.getProductManufacturer())
                .productCategory(productRequestDTO.getProductCategory())
                .productDescription(productRequestDTO.getProductDescription())
                .productPrice(productRequestDTO.getProductPrice())
                .productSN(productRequestDTO.getProductSN())
                .build();
    }
}
