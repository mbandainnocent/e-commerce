package Product_Service.Kafka;

import Product_Service.Model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import product.events.ProductEvent;


@Slf4j
@Service
public class ProductEventPublisher {

    @Value("${kafka.topics.product:product}")
    private String productTopic;


 private final KafkaTemplate< String, byte[]> kafkaTemplate;

    public ProductEventPublisher(KafkaTemplate<String,
            byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void productEventCreated(Product product) {
       ProductEvent productEvent = ProductEvent.newBuilder()
               .setProductId(product.getProductId().toString())
               .setName(product.getProductName())
               .setDescription(product.getProductDescription())
               .setEventType("PRODUCT_CREATED")
               .build();

       try {
           log.info("sending product event {}", productEvent);
          kafkaTemplate.send(productTopic, productEvent.toByteArray());

           log.info("Successfully sent product event for product ID: {}", product.getProductId());
       } catch (Exception e){
           log.error("Exception occurred while sending event", productEvent,e);
       }
    }

}
