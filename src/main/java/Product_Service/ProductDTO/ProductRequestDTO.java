package Product_Service.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    private String productName;

    @NotBlank(message = "Manufacturer name is required")
    @Size(max = 100, message = "Manufacturer name cannot exceed 100 characters")
    private String productManufacture;

    @NotBlank(message = "Product category is required")
    private String productCategory;

    @NotBlank(message = "Product description is required")
    private String productDescription;

    @NotNull()
    @Positive(message = "Price must be a positive number")
    private BigDecimal productPrice;

    @NotBlank(message = "product_SN is required")
    private String productSN;



}