package Product_Service.Outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox", schema = "product_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "event_payload", nullable = false, columnDefinition = "TEXT")
    private String eventPayload;

    @Column(name = "event_metadata", columnDefinition = "TEXT")
    private String eventMetadata;

    @Column(name = "event_status", nullable = false)
    @Builder.Default
    private String eventStatus = "PENDING";

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.eventStatus == null) {
            this.eventStatus = "PENDING";
        }
        
        // Debug logging
        System.out.println("=== OutboxEvent PrePersist ===");
        System.out.println("aggregateType: " + this.aggregateType);
        System.out.println("aggregateId: " + this.aggregateId);
        System.out.println("eventType: " + this.eventType);
        System.out.println("eventStatus: " + this.eventStatus);
        System.out.println("createdAt: " + this.createdAt);
        
        if (this.aggregateType == null) {
            throw new IllegalStateException("aggregateType cannot be null when saving OutboxEvent");
        }
    }
}

