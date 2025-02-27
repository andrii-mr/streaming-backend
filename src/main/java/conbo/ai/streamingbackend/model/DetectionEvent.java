package conbo.ai.streamingbackend.model;

import conbo.ai.streamingbackend.bus.Event;
import conbo.ai.streamingbackend.bus.Topic;
import conbo.ai.streamingbackend.bus.annotation.TopicAlias;
import conbo.ai.streamingbackend.dto.GeoLocation;
import java.time.Instant;
import lombok.NonNull;

@TopicAlias(Topic.DETECTION_TOPIC)
public record DetectionEvent(
    @NonNull Instant timestamp,
    @NonNull String sourceDevice,
    @NonNull String uniqueObjectId,
    @NonNull GeoLocation location,
    @NonNull String objectType,
    float confidence)
    implements Event {}
