package Product_Service.Kafka;

import Product_Service.Model.Product;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductEventPublisherTest {

    @InjectMocks
    private ProductEventPublisher productEventPublisher;

    @Mock
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                productEventPublisher,
                "productTopic",
                "product"
        );
    }

    @Test
    void testPublishProductEventIsSuccessful(){

        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("IPhone");
        product.setProductDescription(" apple smartphone");

        CompletableFuture<SendResult<String, byte[]>> future =
                CompletableFuture.completedFuture(null);

        when(kafkaTemplate.send(anyString(), any(byte[].class)))
                .thenReturn(future);

        //act
        productEventPublisher.productEventCreated(product);

        //asserts
        verify(kafkaTemplate, times(1))
                .send(anyString(), any(byte[].class));

    }

}