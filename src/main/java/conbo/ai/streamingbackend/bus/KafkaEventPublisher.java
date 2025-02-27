package conbo.ai.streamingbackend.bus;

import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

  @NonNull private final KafkaTemplate<String, Event> kafkaTemplate;

  @Override
  public @NonNull <E extends Event, K extends String> CompletableFuture<Void> sendMessage(
      @NonNull E event, @NonNull K key) {
    return kafkaTemplate
        .send(event.getTopic().topicName(), key, event)
        .toCompletableFuture()
        .thenApply(result -> null);
  }
}
