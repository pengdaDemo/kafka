package com.kafka.code.Controller;

import com.kafka.code.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControler {
    @Autowired
    KafkaTemplate kafkaTemplate;
    @GetMapping("/hello")
    public String hello(){
        kafkaSink();
        return "hello word";
    }
    @KafkaListener(topics = "test")
    private void kafka(ConsumerRecord<?, ?> consumer){
        System.out.println(consumer.value());
    }

    private void kafkaSink() {
        new KafkaService(kafkaTemplate).execute();
    }
}
