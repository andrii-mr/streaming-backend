package conbo.ai.streamingbackend.service;

import conbo.ai.streamingbackend.bus.EventPublisher;
import conbo.ai.streamingbackend.dto.DetectionsBatchRequest;
import conbo.ai.streamingbackend.mapper.DetectionMapper;
import conbo.ai.streamingbackend.model.DetectionEvent;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IngestionService {

  @NonNull private final DetectionMapper detectionMapper;

  @NonNull private final EventPublisher eventPublisher;

  public void processBatch(@NonNull DetectionsBatchRequest request) {
    CompletableFuture.allOf(
            detectionMapper.toDetectionEvents(request).stream()
                .map(this::publish)
                .toArray(CompletableFuture[]::new))
        .thenRun(() -> logOnSuccess(request))
        .exceptionally(exception -> logOnError(request, exception));
  }

  private @NonNull CompletableFuture<Void> publish(@NonNull DetectionEvent event) {
    return eventPublisher
        .sendMessage(event, event.sourceDevice())
        .toCompletableFuture()
        .thenAccept(result -> log.debug("Event {} sent successfully", event.uniqueObjectId()))
        .exceptionally(
            exception -> {
              log.error("Failed to send event {}", event.uniqueObjectId(), exception);
              return null;
            });
  }

  private static void logOnSuccess(@NonNull DetectionsBatchRequest request) {
    if (request.detections().isEmpty()) {
      log.info(
          "No detections to send from device {}, timestamp {}",
          request.sourceDevice(),
          request.timestamp());
    } else {
      log.info(
          "All events processed successfully for batch from device {}, timestamp {}, batch size {}",
          request.sourceDevice(),
          request.timestamp(),
          request.detections().size());
    }
  }

  private static Void logOnError(
      @NonNull DetectionsBatchRequest request, @NonNull Throwable exception) {
    log.error(
        "Error processing batch from device {}, timestamp {}, batch size {}",
        request.sourceDevice(),
        request.timestamp(),
        request.detections().size(),
        exception);
    return null;
  }
}
