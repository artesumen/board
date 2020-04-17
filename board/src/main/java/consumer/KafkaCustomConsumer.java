package consumer;


import fish.payara.cloud.connectors.kafka.api.KafkaListener;
import fish.payara.cloud.connectors.kafka.api.OnRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "testClient"),
        @ActivationConfigProperty(propertyName = "groupIdConfig", propertyValue = "testGroup"),
        @ActivationConfigProperty(propertyName = "topics", propertyValue = "test"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/ReRatting_Queue"),
        @ActivationConfigProperty(propertyName = "bootstrapServersConfig", propertyValue = "localhost:9092"),
        @ActivationConfigProperty(propertyName = "autoCommitInterval", propertyValue = "100"),
        @ActivationConfigProperty(propertyName = "retryBackoff", propertyValue = "1000"),
        @ActivationConfigProperty(propertyName = "keyDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
        @ActivationConfigProperty(propertyName = "valueDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
        @ActivationConfigProperty(propertyName = "pollInterval", propertyValue = "30000"),
})
public class KafkaCustomConsumer implements KafkaListener{

    public KafkaCustomConsumer() {
    }

    @OnRecord( topics={"test"})
    public void test(ConsumerRecord<Object,Object> record) {
        System.out.println("Payara Kafka MDB record " + record );

    }
//
//    @Override
//    public void onMessage(Message message) {
//        System.out.println("Message:  "+message);
//
//    }
}
