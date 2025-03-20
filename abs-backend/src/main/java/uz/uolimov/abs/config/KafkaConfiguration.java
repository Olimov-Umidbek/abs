package uz.uolimov.abs.config;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import uz.uolimov.abs.config.props.TransactionKafkaProperties;
import uz.uolimov.abs.model.dto.kafka.TransactionEvent;

import java.util.Map;

@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "abs", value = "is-standalone", havingValue = "false")
public class KafkaConfiguration {
    private final KafkaProperties kafkaProperties;
    private final TransactionKafkaProperties transactionKafkaProperties;

    @Bean("transactionContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> transactionContainerFactory(
        @Qualifier("transactionConsumerFactory")
        ConsumerFactory<String, String> transactionConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> transactionConsumerFactory() {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();
        properties.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            transactionKafkaProperties.consumers().transaction().bootstrapServer()
        );
        properties.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            transactionKafkaProperties.consumers().transaction().groupId()
        );
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> retryContainerFactory(
        @Qualifier("retryConsumerFactory")
        ConsumerFactory<String, String> retryContainerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(retryContainerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> retryConsumerFactory() {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();
        properties.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            transactionKafkaProperties.consumers().retry().bootstrapServer()
        );
        properties.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            transactionKafkaProperties.consumers().retry().groupId()
        );
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        return new DefaultKafkaConsumerFactory<>(properties);
    }


    @Bean
    public KafkaTemplate<String, TransactionEvent> producer() {
        Map<String, Object> properties = kafkaProperties.buildProducerProperties();
        properties.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            transactionKafkaProperties.producers().transaction().bootstrapServer()
        );
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        DefaultKafkaProducerFactory<String, TransactionEvent> producerFactory = new DefaultKafkaProducerFactory<>(properties);

        return new KafkaTemplate<>(producerFactory);
    }
}
