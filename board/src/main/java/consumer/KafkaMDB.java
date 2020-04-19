package consumer;


import converter.FromJsonToDtoConverter;
import dto.DriverStatusDTO;
import dto.TruckStatusDTO;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import websocket.WebSocketService;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Properties;


@Named
@Singleton
@AccessTimeout(value=10000)
public class KafkaMDB implements Serializable {

    @Inject
    private WebSocketService socketService;

    private final static String TOPIC = "test";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private Consumer<Long, String> consumer;

    private DriverStatusDTO driverStatus;

    private TruckStatusDTO truckStatus;


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

    @Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
    public void consume() throws IOException {
        final int giveUp = 100;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1);
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            for (ConsumerRecord<Long, String> consumerRecord : consumerRecords) {
                if (consumerRecord.value().contains("totalDrivers")) {
                    if (FromJsonToDtoConverter.convertToDriverStatusDto(consumerRecord.value()) != null) {
                        driverStatus = FromJsonToDtoConverter.convertToDriverStatusDto(consumerRecord.value());
                            socketService.broadcast(consumerRecord.value());
                        consumer.commitAsync();
                    }

                } else if (consumerRecord.value().contains("totalTrucksNumber")) {
                    if (FromJsonToDtoConverter.convertToTruckStatusDto(consumerRecord.value()) != null) {

                        truckStatus = FromJsonToDtoConverter.convertToTruckStatusDto(consumerRecord.value());
                        consumer.commitAsync();
                            socketService.broadcast(consumerRecord.value());
                    }
                }
                System.out.println("Event  " + consumerRecord.value() + " fired.....");
            }
        }
    }


    private long getCurrentOffset() {
        TopicPartition topicPartition = new TopicPartition(TOPIC, 1);
        consumer.poll(0);
        consumer.seekToEnd(Collections.singletonList(topicPartition));
        return consumer.position(topicPartition) - 1;
    }

    public DriverStatusDTO getDriverStatus() {
        if (driverStatus == null) {
            driverStatus= new DriverStatusDTO();//change to lastOffset for 1st ON
        }
        return driverStatus;
    }

    public TruckStatusDTO getTruckStatus() {
        if (truckStatus == null) {
            truckStatus = new TruckStatusDTO();//change to lastOffset for 1st ON
        }
        return truckStatus;
    }
}
