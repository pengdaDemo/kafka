package com.kafka.code.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * kafka+熔断器
 */
public class KafkaService extends HystrixCommand<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);
    private KafkaTemplate<String, String> kafkaTemplate;


    public KafkaService(KafkaTemplate kafkaTemplate) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(KafkaService.class.getName()))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(10)
                        .withCircuitBreakerSleepWindowInMilliseconds(1000 * 60)
                        .withExecutionTimeoutInMilliseconds(1000)));
        this.kafkaTemplate=kafkaTemplate;
    }

    @Override
    protected Boolean run() throws Exception {
        try {
            kafkaTemplate.send("test", "111111", "写入的kafka值");
            return true;
        } catch (Exception ex) {
            LOGGER.error("sink to kafka error, message: " + ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    protected Boolean getFallback() {
        return true;
    }
}
