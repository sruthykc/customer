package com.diviso.graeshoppe.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configures Spring Cloud Stream support.
 *
 * This works out-of-the-box if you use the Docker Compose configuration at "src/main/docker/kafka.yml".
 *
 * See http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/
 * for the official Spring Cloud Stream documentation.
 */
@EnableBinding(value = { MessageBinderConfiguration.class })
public class MessagingConfiguration {

    @Value("${spring.application.name:JhipsterService}")
    private String applicationName;

    /**
     * This sends a test message at regular intervals set as fixedRate (in ms)
     *
     * In order to see the test messages, you can use the Kafka command-line client:
     * "./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic-jhipster --from-beginning".
     */
//    @Bean
//    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedRate = "60000"))
//    public MessageSource<String> timerMessageSource() {
//        return () -> new GenericMessage<>("Test message from " + applicationName
//            + " sent at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//    }
}
