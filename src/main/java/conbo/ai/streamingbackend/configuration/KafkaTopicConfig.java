package conbo.ai.streamingbackend.configuration;

import conbo.ai.streamingbackend.bus.Topic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

  @Bean
  public NewTopic detectionTopic() {
    return new NewTopic(Topic.DETECTION_TOPIC.topicName(), 10, (short) 1);
  }
}
