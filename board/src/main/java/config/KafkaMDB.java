//package config;
//
//
//import fish.payara.cloud.connectors.kafka.api.OnRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.jboss.annotation.ejb.Consumer;
//import org.jboss.annotation.ejb.ResourceAdapter;
//
//import javax.ejb.ActivationConfigProperty;
//import javax.ejb.MessageDriven;
//
//@MessageDriven(activationConfig = {
//        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "KafkaJCAClient"),
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
//@ResourceAdapter(value="kafka")
//@Consumer
//public class KafkaMDB {
//    public KafkaMDB() {
//        System.out.println("Bean instance created");
//    }
//
//
//    @OnRecord( topics={"test"})
//    public void getMessage(ConsumerRecord record) {
//        System.out.println("> Got record on topic test " + record);
//    }
//}
