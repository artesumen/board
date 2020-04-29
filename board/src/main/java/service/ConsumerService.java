package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.BoardOrderStatusDTO;
import dto.DriverStatusDTO;
import dto.TruckStatusDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.log4j.Logger;
import rest.RestClient;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


@Named("consumerService")
@Singleton
@AccessTimeout(value = 7000)
public class ConsumerService {
    @Inject
    private WebSocketService socketService;
    @Inject
    private RestClient client;
    private List<BoardOrderStatusDTO> orderStatus;
    private DriverStatusDTO driverStatus;
    private TruckStatusDTO truckStatus;
    private static final Logger LOG = Logger.getLogger(ConsumerService.class);
    private ObjectMapper mapper = new ObjectMapper();

    public void getMessageAndWriteToFile(ConsumerRecords<Long, String> consumerRecords) {
        int noRecordsCount = 0;
        int giveUp = 100;

        while (true) {
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            for (ConsumerRecord<Long, String> consumerRecord : consumerRecords) {
                if (consumerRecord.value().contains("drivers")) {
                    try (FileWriter writer = new FileWriter("C:\\Users\\Admin\\Desktop\\Java" +
                            "\\Java school board\\board\\src\\main\\" +
                            "resources\\META-INF\\driverStatus.txt")) {

                        driverStatus = client.getDriverStatus();
                        LOG.info("TOTAL DRIVERS"+driverStatus.getTotalDrivers());
                        writer.write(mapper.writeValueAsString(driverStatus));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socketService.broadcast("driver updated");

                } else if (consumerRecord.value().contains("trucks")) {

                    try (FileWriter writer = new FileWriter("C:\\Users\\Admin\\Desktop\\Java" +
                            "\\Java school board\\board\\src\\main\\" +
                            "resources\\META-INF\\truckStatus.txt")) {
                        truckStatus = client.getTruckStatus();
                        LOG.info("TOTAL TRUCKS"+truckStatus.getTotalTrucksNumber());
                        writer.write(mapper.writeValueAsString(truckStatus));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socketService.broadcast("truck updated");
                } else if(consumerRecord.value().contains("orders")) {
                    try(FileWriter writer = new FileWriter("C:\\Users\\Admin\\Desktop\\Java" +
                            "\\Java school board\\board\\src\\main\\" +
                            "resources\\META-INF\\orderStatus.txt")){
                        orderStatus= client.getOrderStatus();
                        LOG.info("TOTAL ORDERS"+orderStatus.size());
                        writer.write(mapper.writeValueAsString(orderStatus));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socketService.broadcast("order updated");

                }
                LOG.info("Event  " + consumerRecord.value() + " offset " + consumerRecord.offset() + " fired.....");
            }
            break;
        }
    }

    public DriverStatusDTO getDriverStatus() {
        if (driverStatus == null) {
            driverStatus = client.getDriverStatus();
        }
        return driverStatus;
    }

    public Long getTotalTrucksNumber(){
        return getTruckStatus().getTotalTrucksNumber();
    }

    public Long getRestTrucksNumber(){
        return getTruckStatus().getTotalRestNumber();
    }

    public Long getBrokenTrucksNumber(){
        return getTruckStatus().getTotalBrokenNumber();
    }



    public TruckStatusDTO getTruckStatus() {
        if (truckStatus == null) {
            truckStatus = client.getTruckStatus();
        }
        return truckStatus;
    }

    public List<BoardOrderStatusDTO> getOrderStatus() {
        if(orderStatus == null){
            orderStatus = client.getOrderStatus();
        }
        return orderStatus;
    }
}
