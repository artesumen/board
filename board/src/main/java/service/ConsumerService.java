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
                if (consumerRecord.value().contains("driver")) {
                    driverStatus = client.getDriverStatus();
                    socketService.broadcast("driver updated");
                    LOG.info("TOTAL DRIVERS" + driverStatus.getTotalDrivers());

                } else if (consumerRecord.value().contains("trucks")) {
                    truckStatus = client.getTruckStatus();
                    socketService.broadcast("truck updated");
                    LOG.info("TOTAL TRUCKS" + truckStatus.getTotalTrucksNumber());
                } else if (consumerRecord.value().contains("orders")) {
                    orderStatus = client.getOrderStatus();
                    socketService.broadcast("order updated");
                    LOG.info("TOTAL ORDERS" + orderStatus.size());
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

    public Long getTotalTrucksNumber() {
        return getTruckStatus().getTotalTrucksNumber();
    }

    public Long getRestTrucksNumber() {
        return getTruckStatus().getTotalRestNumber();
    }

    public Long getBrokenTrucksNumber() {
        return getTruckStatus().getTotalBrokenNumber();
    }


    public TruckStatusDTO getTruckStatus() {
        if (truckStatus == null) {
            truckStatus = client.getTruckStatus();
        }
        return truckStatus;
    }

    public List<BoardOrderStatusDTO> getOrderStatus() {
        if (orderStatus == null) {
            orderStatus = client.getOrderStatus();
        }
        return orderStatus;
    }
}
