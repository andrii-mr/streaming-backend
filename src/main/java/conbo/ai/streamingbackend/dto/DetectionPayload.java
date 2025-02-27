package conbo.ai.streamingbackend.dto;

import lombok.NonNull;

public record DetectionPayload(
    @NonNull String uniqueObjectId,
    @NonNull GeoLocation location,
    @NonNull String objectType,
    float confidence) {}
