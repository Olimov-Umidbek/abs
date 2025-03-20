package uz.uolimov.abs.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.Period;

@ConfigurationProperties("abs")
public record ApplicationProperties(
    Duration transactionTtl,
    Integer retryCount,
    Period monthOfKeepingRecords
) { }
