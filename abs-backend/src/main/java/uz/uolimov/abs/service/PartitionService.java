package uz.uolimov.abs.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uz.uolimov.abs.config.props.ApplicationProperties;
import uz.uolimov.abs.model.enums.PartitionedTable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Slf4j
@Service
@AllArgsConstructor
public class PartitionService {

    private final ApplicationProperties properties;
    private final JdbcTemplate jdbcTemplate;

    private static final Pattern PARTITION_NAME_PATTERN = Pattern.compile(".*(\\d{4}_\\d{2}_\\d{2})_.*._((\\d{4}_\\d{2}_\\d{2}))");
    private static final DateTimeFormatter PARTITION_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    public void recyclePartitions() {

        dropOldPartitions(PartitionedTable.TRANSACTIONS.getName());
        dropOldPartitions(PartitionedTable.TRANSACTION_HISTORIES.getName());
        log.info("Old partitions dropped");

        createPartitions(PartitionedTable.TRANSACTIONS.getName());
        createPartitions(PartitionedTable.TRANSACTION_HISTORIES.getName());
        log.info("Created new partitions");
    }

    private void dropOldPartitions(String tableName) {
        long monthsToKeep = properties.monthOfKeepingRecords().toTotalMonths();
        LocalDate lastDayToKeep = LocalDate.now().minusMonths(monthsToKeep);

        getAllPartition(tableName)
            .stream()
            .map (it -> LocalDate.parse(it, PARTITION_DATE_FORMATTER) )
            .filter ( it -> it.isBefore(lastDayToKeep) )
            .map (PARTITION_DATE_FORMATTER::format)
            .map (it -> tableName + "_" + it)
            .forEach(this::dropPartition);
    }

    private void createPartitions(String tableName) {
        List<String> partitions = getAllPartition(tableName);
        LocalDate currentMonday = LocalDate.now().with(previousOrSame(DayOfWeek.MONDAY));
        LocalDate nextMonday = currentMonday.plusWeeks(1L);

        Set<String> existingPartitions = partitions
            .stream()
            .map(PARTITION_NAME_PATTERN::matcher)
            .filter(Matcher::matches)
            .map(it -> it.group(1))
            .collect(Collectors.toSet());

        if (!existingPartitions.contains(PARTITION_DATE_FORMATTER.format(currentMonday))) {
            create(tableName, currentMonday);
        }

        if (!existingPartitions.contains(PARTITION_DATE_FORMATTER.format(nextMonday))) {
            create(tableName, nextMonday);
        }
    }

    private void create(String tableName, LocalDate date) {
        String firstDateText = date.with(previousOrSame(DayOfWeek.MONDAY)).format(PARTITION_DATE_FORMATTER);
        LocalDate lastDate = date.with(nextOrSame(DayOfWeek.SUNDAY));
        String lastDateText = lastDate.format(PARTITION_DATE_FORMATTER);
        String nextFirstDateText = lastDate.plusDays(1).format(PARTITION_DATE_FORMATTER);
        String partitionName = tableName + "_" + firstDateText + "_to_" + lastDateText;
        String sql = "CREATE TABLE IF NOT EXISTS " + partitionName + " PARTITION OF " + tableName +
            " FOR VALUES FROM ('" + firstDateText + "') TO ('" + nextFirstDateText+ "')";

        try {
            jdbcTemplate.update (sql);
            log.info("Partition [" + partitionName + "] was created");
        } catch (Exception e) {
            log.error("Partition [" + partitionName + "] was not created");
            log.error(e.getMessage(), e);
        }
    }


    private List<String> getAllPartition(String tableName) {
        return jdbcTemplate.queryForList(
            "SELECT child.relname AS child " +
                "FROM pg_inherits " +
                "JOIN pg_class parent ON pg_inherits.inhparent = parent.oid " +
                "JOIN pg_class child ON pg_inherits.inhrelid = child.oid " +
                "WHERE parent.relname = '" + tableName + "'",
            String.class
        );
    }

    private void dropPartition(String partition) {
        try {
            jdbcTemplate.update ("DROP TABLE IF EXISTS " + partition);
            log.info("Partition ["+ partition+"] was deleted");
        } catch (Exception e) {
            log.error("Partition [" + partition + "] was not deleted");
            log.error(e.getMessage(), e);
        }
    }
}
