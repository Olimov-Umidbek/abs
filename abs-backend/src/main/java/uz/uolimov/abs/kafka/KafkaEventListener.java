package uz.uolimov.abs.kafka;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import uz.uolimov.abs.kafka.processor.BaseEventProcessor;
import uz.uolimov.abs.model.dto.kafka.TransactionEvent;
import uz.uolimov.abs.model.enums.TransactionTechStatus;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnBean(name = "distributedTransactionServiceImpl")
public class KafkaEventListener {
    private final List<BaseEventProcessor> processorList;
    private Map<TransactionTechStatus, BaseEventProcessor> processorMap;

    @PostConstruct
    public void init() {
        processorMap = processorList
            .stream()
            .collect(Collectors.toMap(BaseEventProcessor::status, Function.identity()));
    }

    @KafkaListener(
        containerFactory = "transactionContainerFactory",
        topics = "${spring.kafka.consumers.transaction.topic}",
        groupId = "${spring.kafka.consumers.transaction.group-id}"
    )
    public void transfersListener(ConsumerRecord<String, TransactionEvent> record) {
        log.info("Incoming event from transaction topic with transactionId = " + record.key());
        process(record);
    }

    @KafkaListener(
        containerFactory = "retryContainerFactory",
        topics = "${spring.kafka.consumers.retry.topic}",
        groupId = "${spring.kafka.consumers.retry.group-id}"
    )
    public void retryListener(ConsumerRecord<String, TransactionEvent> record) {
        log.info("Incoming event from retry topic with transactionId = " + record.key());
        process(record);
    }

    private void process(ConsumerRecord<String, TransactionEvent> record) {
        TransactionEvent event = record.value();
        if (processorMap.containsKey(event.status())) {
            processorMap.get(event.status()).process(event);
        } else {
            log.error("Unexpected status in incoming payload, status = " + event.status());
        }
    }
}
