package Product_Service.Mapper;

import Product_Service.Model.Product;
import Product_Service.ProductDTO.ProductRequestDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    @Test
    void toProductModel_shouldMapAllFieldsCorrectly(){
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setProductName("Test Product");
        productRequestDTO.setProductManufacture("Test Manufacturer");
        productRequestDTO.setProductCategory("Electronics");
        productRequestDTO.setProductDescription("Test Description");
        productRequestDTO.setProductPrice(BigDecimal.valueOf(999.99));
        productRequestDTO.setProductSN("SN-12345");

        Product product = ProductMapper.toProductModel(productRequestDTO);

        assertNotNull(product);

        assertEquals("Test Product", product.getProductName());
        assertEquals("Test Manufacturer", product.getProductManufacture());
        assertEquals("Electronics", product.getProductCategory());

    }

//    @Test
//    void toProductModel_shouldHandleNullInput(){
//        //when
//        Product product = ProductMapper.toProductModel(null);
//
//        //then asset
//        assertNull(product);
//    }

    @Test
    void toProductModel_ShouldHandlePartialData(){
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setProductName("Partial Product");
        dto.setProductPrice(BigDecimal.valueOf(100.0));

        Product product = ProductMapper.toProductModel(dto);

        assertNotNull(product);
        assertEquals("Partial Product", product.getProductName());
        assertEquals(0, BigDecimal.valueOf(100.0).compareTo(product.getProductPrice()));
        assertNull(product.getProductManufacture());
        assertNull(product.getProductCategory());
        assertNull(product.getProductDescription());
        assertNull(product.getProductSN());
    }



}