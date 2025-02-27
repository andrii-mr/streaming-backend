package conbo.ai.streamingbackend.mapper;

import conbo.ai.streamingbackend.dto.DetectionPayload;
import conbo.ai.streamingbackend.dto.DetectionsBatchRequest;
import conbo.ai.streamingbackend.model.DetectionEvent;
import java.util.List;
import lombok.NonNull;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetectionMapper {
  DetectionEvent toDetectionEvent(DetectionsBatchRequest request, DetectionPayload payload);

  default @NonNull List<DetectionEvent> toDetectionEvents(@NonNull DetectionsBatchRequest request) {
    return request.detections().stream()
        .map(payload -> toDetectionEvent(request, payload))
        .toList();
  }
}
