package de.hexad.libmanagement.msgbroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class KafkaProducer {

    private static final String TOPIC = "my_topic";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostConstruct
    public void init(){
        System.out.println("Dude ugly!");
    }
    public void writeMessage(String message){
        this.kafkaTemplate.send(TOPIC, message);
    }




}
