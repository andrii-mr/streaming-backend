package conbo.ai.streamingbackend.bus.annotation;

import conbo.ai.streamingbackend.bus.Topic;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TopicAlias {
  Topic value();
}
