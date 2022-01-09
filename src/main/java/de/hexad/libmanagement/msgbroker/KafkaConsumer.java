package de.hexad.libmanagement.msgbroker;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "my_topic")
    public void getMessage(String message){

        System.out.println(message);
    }
}
