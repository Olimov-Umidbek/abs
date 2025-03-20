package uz.uolimov.abs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import uz.uolimov.abs.config.props.ApplicationProperties;
import uz.uolimov.abs.config.props.TransactionKafkaProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({
    ApplicationProperties.class,
    TransactionKafkaProperties.class
})
public class AbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbsApplication.class, args);
    }
}
