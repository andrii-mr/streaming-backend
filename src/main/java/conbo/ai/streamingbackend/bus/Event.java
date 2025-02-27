package conbo.ai.streamingbackend.bus;

import conbo.ai.streamingbackend.bus.annotation.TopicAlias;
import java.util.Optional;
import lombok.NonNull;

public interface Event {
  default @NonNull Topic getTopic() {
    return Optional.ofNullable(this.getClass().getAnnotation(TopicAlias.class))
        .map(TopicAlias::value)
        .orElseThrow(
            () ->
                new IllegalAccessError(
                    "Event class %s does not have @Topic annotation"
                        .formatted(this.getClass().getCanonicalName())));
  }
}
