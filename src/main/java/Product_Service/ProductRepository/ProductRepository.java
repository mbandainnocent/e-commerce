package Product_Service.ProductRepository;

import Product_Service.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByProductSN( String productSN);
}
