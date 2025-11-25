package Product_Service.Exception;

public class ProductAlreadyExistByProductSN extends RuntimeException   {
    public ProductAlreadyExistByProductSN(String message) {
        super(message);
    }
}
