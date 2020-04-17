package consumer;


import converter.FromJsonToDtoConverter;
import dto.DriverStatusDTO;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import websocket.SessionHandler;
import websocket.WebSocketEndpoint;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


@Named
//@ApplicationScoped
@Stateless
@Startup
//@ResourceAdapter(value="kafka")
//@RequestScoped
public class KafkaMDB implements Serializable{

    @Inject
    private WebSocketEndpoint webSocket;

    @Inject
    private SessionHandler sessionHandler;


    private final static String TOPIC = "test";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private Consumer<Long, String> consumer;

    private String message;

    private List<DriverStatusDTO> savedDriver = new ArrayList<>();


    @PostConstruct
    public void createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "KafkaMDB");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        // Create the consumer using props.
        consumer = new KafkaConsumer<>(props);
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));

        System.out.println("post-construct method outside loop working!");

    }

    public KafkaMDB() {
    }

    public void consume(){
        final int giveUp = 100;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1);
//            if (consumerRecords.count() == 0) {
//                noRecordsCount++;
//                if (noRecordsCount > giveUp) break;
//                else continue;
//            }
            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%s, %d, %d)\n",
                        record.value(),
                        record.partition(), record.offset());
            });
//            consumer.commitAsync();
            for (ConsumerRecord<Long, String> consumerRecord : consumerRecords) {
                try {
                    if (!savedDriver.isEmpty()) {
                        savedDriver.clear();
                    }
                    if (FromJsonToDtoConverter.convertToDriverStatusDto(consumerRecord.value()) != null) {
                        savedDriver.add(FromJsonToDtoConverter.convertToDriverStatusDto(consumerRecord.value()));
                        consumer.commitAsync();
                        webSocket.onServerMessage(sessionHandler.getSessions().get(0),consumerRecord.value());
                        break;
                    }
                    System.out.println("Event  " + consumerRecord.value() + " fired.....");
//                return FromJsonToDtoConverter.convertToDriverDto(consumerRecord.value());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //    @OnRecord( topics={"test"})
//    @PostConstruct
//    public void findMessageAndPass() {
//        final int giveUp = 100;
//        int noRecordsCount = 0;
//
//        while (true) {
//            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1);
//            if (consumerRecords.count() == 0) {
//                noRecordsCount++;
//                if (noRecordsCount > giveUp) break;
//                else continue;
//            }
//
//
//            consumerRecords.forEach(record -> {
//                System.out.printf("Consumer Record:(%s, %d, %d)\n",
//                        record.value(),
//                        record.partition(), record.offset());
//            });
//
//            consumer.commitAsync();
//
//
//            for (ConsumerRecord<Long, String> consumerRecord : consumerRecords) {
//                try {
//                    if (!savedDriver.isEmpty()) {
//                        savedDriver.clear();
//                    }
//                    if (FromJsonToDtoConverter.convertToDriverStatusDto(consumerRecord.value()) != null) {
//                        savedDriver.add(FromJsonToDtoConverter.convertToDriverStatusDto(consumerRecord.value()));
//                        webSocket.onServerMessage(sessionHandler.getSessions().get(0),consumerRecord.value());
//                    }
//                    System.out.println("Event  " + consumerRecord.value() + " fired.....");
////                return FromJsonToDtoConverter.convertToDriverDto(consumerRecord.value());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
////        consumer.close();
//    }

    private long getCurrentOffset(){
        TopicPartition topicPartition = new TopicPartition(TOPIC,1);
        consumer.poll(0);
        consumer.seekToEnd(Collections.singletonList(topicPartition));
        return consumer.position(topicPartition) -1;
    }


    public List<DriverStatusDTO> getSavedDriver() {
//        consume();
        if(savedDriver.isEmpty())
        {
            savedDriver.add(new DriverStatusDTO());//change to lastOffset for 1st ON
        }
        return savedDriver;
    }

    public String getMessage() {

        return null;
    }
}
