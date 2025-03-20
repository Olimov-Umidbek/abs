package uz.uolimov.abs.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import uz.uolimov.abs.config.props.TransactionKafkaProperties;
import uz.uolimov.abs.model.dto.kafka.TransactionEvent;
import uz.uolimov.abs.model.enums.TransactionTechStatus;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(prefix = "abs", value = "is-standalone", havingValue = "false")
public class ProducerService {

    private final TransactionKafkaProperties properties;
    private final KafkaTemplate<String, TransactionEvent> producer;

    public void sendToKafka(TransactionEvent event) {
        if (event.status() == TransactionTechStatus.RETRY) {
            sendToKafka(event, properties.producers().retry().topic());
        } else {
            sendToKafka(event, properties.producers().transaction().topic());
        }
    }

    private void sendToKafka(TransactionEvent event, String topic) {
        ProducerRecord<String, TransactionEvent> record = new ProducerRecord<>(topic, event.transactionId().toString(), event);
        producer.send(record);
    }

}
