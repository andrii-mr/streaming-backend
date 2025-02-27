package conbo.ai.streamingbackend.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import conbo.ai.streamingbackend.bus.EventPublisher;
import conbo.ai.streamingbackend.dto.DetectionPayload;
import conbo.ai.streamingbackend.dto.DetectionsBatchRequest;
import conbo.ai.streamingbackend.dto.GeoLocation;
import conbo.ai.streamingbackend.mapper.DetectionMapper;
import conbo.ai.streamingbackend.model.DetectionEvent;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IngestionServiceTest {

  @Mock private DetectionMapper detectionMapper;

  @Mock private EventPublisher eventPublisher;

  @InjectMocks private IngestionService ingestionService;

  private LogCaptor logCaptor;

  @BeforeEach
  void setUp() {
    logCaptor = LogCaptor.forClass(IngestionService.class);
  }

  @Test
  void processBatch_allEventsSentSuccessfully() {
    String sourceDevice = "camera-123";
    Instant timestamp = Instant.now();
    List<DetectionPayload> detections = createDetections();
    DetectionsBatchRequest request =
        new DetectionsBatchRequest(sourceDevice, timestamp, detections);
    List<DetectionEvent> events = createDetectionEvents(timestamp, sourceDevice);

    when(detectionMapper.toDetectionEvents(request)).thenReturn(events);
    when(eventPublisher.sendMessage(any(DetectionEvent.class), eq(sourceDevice)))
        .thenReturn(CompletableFuture.completedFuture(null));

    ingestionService.processBatch(request);

    verify(detectionMapper, times(1)).toDetectionEvents(request);
    verify(eventPublisher, times(2)).sendMessage(any(DetectionEvent.class), eq(sourceDevice));

    assertTrue(
        logCaptor.getInfoLogs().stream()
            .anyMatch(log -> log.contains("All events processed successfully for batch")));
  }

  @Test
  void processBatch_someEventsFail() {
    String sourceDevice = "camera-123";
    Instant timestamp = Instant.now();
    List<DetectionPayload> detections = createDetections();
    DetectionsBatchRequest request =
        new DetectionsBatchRequest(sourceDevice, timestamp, detections);
    List<DetectionEvent> events = createDetectionEvents(timestamp, sourceDevice);

    when(detectionMapper.toDetectionEvents(request)).thenReturn(events);

    when(eventPublisher.sendMessage(eq(events.get(0)), eq(sourceDevice)))
        .thenReturn(CompletableFuture.completedFuture(null));
    when(eventPublisher.sendMessage(eq(events.get(1)), eq(sourceDevice)))
        .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Sending failed")));

    ingestionService.processBatch(request);

    verify(eventPublisher, times(2)).sendMessage(any(DetectionEvent.class), eq(sourceDevice));

    assertTrue(
        logCaptor.getErrorLogs().stream().anyMatch(log -> log.contains("Failed to send event")));
  }

  @Test
  void processBatch_noDetections_emptyList() {
    String sourceDevice = "camera-123";
    Instant timestamp = Instant.now();
    DetectionsBatchRequest request = new DetectionsBatchRequest(sourceDevice, timestamp, List.of());

    when(detectionMapper.toDetectionEvents(request)).thenReturn(List.of());

    ingestionService.processBatch(request);

    assertTrue(
        logCaptor.getInfoLogs().stream()
            .anyMatch(log -> log.contains("No detections to send from device")));

    verify(detectionMapper, times(1)).toDetectionEvents(request);
    verify(eventPublisher, never()).sendMessage(any(DetectionEvent.class), anyString());
  }

  private List<DetectionPayload> createDetections() {
    return List.of(
        new DetectionPayload("object-1", new GeoLocation(52.5200f, 13.4050f), "car", 0.9f),
        new DetectionPayload("object-2", new GeoLocation(48.8566f, 2.3522f), "person", 0.8f));
  }

  private List<DetectionEvent> createDetectionEvents(Instant timestamp, String sourceDevice) {
    return List.of(
        new DetectionEvent(
            timestamp, sourceDevice, "object-1", new GeoLocation(52.5200f, 13.4050f), "car", 0.9f),
        new DetectionEvent(
            timestamp,
            sourceDevice,
            "object-2",
            new GeoLocation(48.8566f, 2.3522f),
            "person",
            0.8f));
  }
}
