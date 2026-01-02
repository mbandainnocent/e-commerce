package Product_Service.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product", schema = "product_schema")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")

    @NotNull
    @Column(name = "productId", nullable = false,columnDefinition = "uuid")

    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_manufacturer", nullable = false)
    private String productManufacture;

    @NotNull
    @Column(name = "product_category", nullable = false)
    private String productCategory;

    @NotNull
    @Column(name = "product_description", nullable = false)
    private String productDescription;


    @Column(name = "product_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal productPrice;
    @NotNull
    @Column(name = "product_sn", nullable = false)
    private String productSN;


}
