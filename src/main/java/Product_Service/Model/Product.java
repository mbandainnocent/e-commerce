package Product_Service.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "product_table", schema = "product_schema")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    @Column(name = "productId", nullable = false)
    private UUID productId;

    @Column(name = "productName", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_canufacturer", nullable = false)
    private String productManufacturer;

    @NotNull
    @Column(name = "product_category", nullable = false)
    private String productCategory;

    @NotNull
    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @NotNull
    @Column(name = "product_price", nullable = false)
    private double productPrice;

    @NotNull
    @Column(name = "product_SN", nullable = false)
    private String productSN;


}
