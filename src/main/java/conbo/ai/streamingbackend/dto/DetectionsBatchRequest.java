package conbo.ai.streamingbackend.dto;

import java.time.Instant;
import java.util.List;
import lombok.NonNull;

public record DetectionsBatchRequest(
    @NonNull String sourceDevice,
    @NonNull Instant timestamp,
    @NonNull List<DetectionPayload> detections) {}
