package uz.uolimov.abs.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.kafka")
public record TransactionKafkaProperties(
    Consumers consumers,
    Producers producers
) {

    public record Consumers(
        ConsumerProperties transaction,
        ConsumerProperties retry
    ) {}

    public record Producers(
        ProducerProperties transaction,
        ProducerProperties retry
    ) {
    }

    public record ConsumerProperties(
        String bootstrapServer,
        String topic,
        String groupId
    ) {}

    public record ProducerProperties(
        String bootstrapServer,
        String topic
    ) {}


}
