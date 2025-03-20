package uz.uolimov.abs.kafka.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.uolimov.abs.kafka.TransactionManagerService;
import uz.uolimov.abs.model.dto.kafka.TransactionEvent;
import uz.uolimov.abs.model.enums.TransactionTechStatus;

@Slf4j
@Component
public class RollbackEventProcessor extends BaseEventProcessor {

    public RollbackEventProcessor(TransactionManagerService service) {
        super(service);
    }

    @Override
    public void process(TransactionEvent event) {
        service.handleRollbackEvent(event);
    }

    @Override
    public TransactionTechStatus status() {
        return TransactionTechStatus.ROLLBACK;
    }
}
