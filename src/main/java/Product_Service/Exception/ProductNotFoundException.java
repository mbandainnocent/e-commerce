package Product_Service.Exception;

public class ProductNotFoundException extends RuntimeException  {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
