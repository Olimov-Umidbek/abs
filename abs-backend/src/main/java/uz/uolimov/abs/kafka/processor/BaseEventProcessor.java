package uz.uolimov.abs.kafka.processor;

import uz.uolimov.abs.kafka.TransactionManagerService;
import uz.uolimov.abs.model.dto.kafka.TransactionEvent;
import uz.uolimov.abs.model.enums.TransactionTechStatus;

public abstract class BaseEventProcessor {

    protected final TransactionManagerService service;

    public BaseEventProcessor(TransactionManagerService service) {
        this.service = service;
    }

    public abstract TransactionTechStatus status();

    public abstract void process(TransactionEvent event);
}
