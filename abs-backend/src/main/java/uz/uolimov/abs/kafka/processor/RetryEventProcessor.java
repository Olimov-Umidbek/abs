package uz.uolimov.abs.kafka.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import uz.uolimov.abs.kafka.TransactionManagerService;
import uz.uolimov.abs.model.dto.kafka.TransactionEvent;
import uz.uolimov.abs.model.enums.TransactionTechStatus;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "abs", value = "is-standalone", havingValue = "false")
public class RetryEventProcessor extends BaseEventProcessor {

    public RetryEventProcessor(TransactionManagerService service) {
        super(service);
    }

    @Override
    public void process(TransactionEvent event) {
        service.handleRetryEvent(event);
    }

    @Override
    public TransactionTechStatus status() {
        return TransactionTechStatus.RETRY;
    }
}
