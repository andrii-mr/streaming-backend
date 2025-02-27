package conbo.ai.streamingbackend.bus;

import lombok.NonNull;

public enum Topic {
  DETECTION_TOPIC;

  public @NonNull String topicName() {
    return name().toLowerCase();
  }
}
