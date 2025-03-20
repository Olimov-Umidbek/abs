package uz.uolimov.abs.schedulers;

import lombok.AllArgsConstructor;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.uolimov.abs.service.PartitionService;

@Component
@ConditionalOnProperty(prefix = "abs.partition", value = "enabled", havingValue = "true")
@AllArgsConstructor
public class PartitionScheduler {
    private final PartitionService partitionService;

    @Scheduled(cron = "${abs.partition.cron}")
    @SchedulerLock(name = "lock_partitions", lockAtLeastFor = "PT1M", lockAtMostFor = "PT15M")
    public void recyclePartitions() {
        LockAssert.assertLocked();
        partitionService.recyclePartitions();
    }
}
