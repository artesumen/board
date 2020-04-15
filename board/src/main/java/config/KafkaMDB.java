package config;


import converter.FromJsonToDtoConverter;
import dto.DriverDTO;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

//@MessageDriven(activationConfig = {
//        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "testClient"),
//        @ActivationConfigProperty(propertyName = "groupIdConfig", propertyValue = "myGroup"),
//        @ActivationConfigProperty(propertyName = "topics", propertyValue = "test"),
//        @ActivationConfigProperty(propertyName = "bootstrapServersConfig", propertyValue = "localhost:9092"),
//        @ActivationConfigProperty(propertyName = "retryBackoff", propertyValue = "1000"),
//        @ActivationConfigProperty(propertyName = "autoCommitInterval", propertyValue = "100"),
//        @ActivationConfigProperty(propertyName = "keyDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
//        @ActivationConfigProperty(propertyName = "valueDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
//        @ActivationConfigProperty(propertyName = "pollInterval", propertyValue = "3000"),
//        @ActivationConfigProperty(propertyName = "commitEachPoll", propertyValue = "true"),
//        @ActivationConfigProperty(propertyName = "useSynchMode", propertyValue = "true")
//})
@Named
//@ResourceAdapter(value="kafka")
//@Singleton
public class KafkaMDB {

    private final static String TOPIC = "test";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private List<DriverDTO> savedDriver = new ArrayList<>();

    private Consumer<Long, String> createConsumer() {
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
        final Consumer<Long, String> consumer =
                new KafkaConsumer<>(props);
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    public KafkaMDB() {
    }


//    @OnRecord( topics={"test"})
//    @PostConstruct
    public void findMessageAndPass() {
        final int giveUp = 100;
        int noRecordsCount = 0;
        final Consumer<Long, String> consumer = createConsumer();


        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(100);
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }


            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%s, %d, %d)\n",
                        record.value(),
                        record.partition(), record.offset());
            });

            consumer.commitAsync();


            for (ConsumerRecord<Long, String> consumerRecord : consumerRecords) {
                try {
                    savedDriver.add(FromJsonToDtoConverter.convertToDriverDto(consumerRecord.value()));
//                return FromJsonToDtoConverter.convertToDriverDto(consumerRecord.value());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        consumer.close();
    }

    public List<DriverDTO> getSavedDriver() {
        findMessageAndPass();
        return savedDriver;
    }
}
