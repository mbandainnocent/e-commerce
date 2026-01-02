package Product_Service.ProductDTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductResponseDTO {

    private UUID productId;
    private String productName;
    private String productManufacture;
    private String productCategory;
    private String productDescription;
    private BigDecimal productPrice;
    private String productSN;
}
