package andersen.parking.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Value("${kafka.topics.parking}")
    private String topic;

    @Bean
    public NewTopic parkingTopic() {
        return TopicBuilder
                .name(topic)
                .replicas(1)
                .partitions(1)
                .build();
    }
}
