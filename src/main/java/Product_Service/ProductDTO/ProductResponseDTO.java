package Product_Service.ProductDTO;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductResponseDTO {

    private UUID productId;
    private String productName;
    private String productManufacturer;
    private String productCategory;
    private String productDescription;
    private Double productPrice;
    private String productSN;
}
