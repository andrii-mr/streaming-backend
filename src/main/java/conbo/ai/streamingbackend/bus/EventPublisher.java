package conbo.ai.streamingbackend.bus;

import java.util.concurrent.CompletableFuture;
import lombok.NonNull;

public interface EventPublisher {
  @NonNull
  <E extends Event, K extends String> CompletableFuture<Void> sendMessage(
      @NonNull E event, @NonNull K key);
}
