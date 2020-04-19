package consumer;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import service.ConsumerService;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Properties;



@Singleton
public class KafkaConsumer{

    @Inject
    private ConsumerService consumerService;

    private final static String TOPIC = "test";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private Consumer<Long, String> consumer;


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
        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));

        System.out.println("post-construct method outside loop working!");
    }

    public KafkaConsumer() {
    }

    @Schedule(second = "*/2", minute = "*", hour = "*", persistent = false)
    public void consume(){
        consumerService.getMessageAndWriteToFile(consumer.poll(1));
        System.out.println("consume done");
    }


//    private long getCurrentOffset() {
//        TopicPartition topicPartition = new TopicPartition(TOPIC, 1);
//        consumer.poll(0);
//        consumer.seekToEnd(Collections.singletonList(topicPartition));
//        return consumer.position(topicPartition) - 1;
//    }
//
//    public DriverStatusDTO getDriverStatus() {
//        if (driverStatus == null) {
//            driverStatus= new DriverStatusDTO();//change to lastOffset for 1st ON
//        }
//        return driverStatus;
//    }
//
//    public TruckStatusDTO getTruckStatus() {
//        if (truckStatus == null) {
//            truckStatus = new TruckStatusDTO();//change to lastOffset for 1st ON
//        }
//        return truckStatus;
//    }
}
