package Product_Service.Outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OutboxRepo extends JpaRepository<OutboxEvent, UUID> {
}
