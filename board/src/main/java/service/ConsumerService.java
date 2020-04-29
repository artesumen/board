package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import converter.FromJsonToDtoConverter;
import dto.BoardOrderStatusDTO;
import dto.DriverStatusDTO;
import dto.TruckStatusDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import rest.RestClient;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.FileReader;
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
    private ObjectMapper mapper = new ObjectMapper();

    public void getMessageAndWriteToFile(ConsumerRecords<Long, String> consumerRecords) {
        if (consumerRecords == null) {
            System.out.println("null");
            return;
        }
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
                        System.out.println("TOTAL DRIVERS"+driverStatus.getTotalDrivers());
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
                        System.out.println("TOTAL TRUCKS"+truckStatus.getTotalTrucksNumber());
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
                        System.out.println("TOTAL ORDERS"+orderStatus.size());
                        writer.write(mapper.writeValueAsString(orderStatus));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socketService.broadcast("order updated");

                }
                System.out.println("Event  " + consumerRecord.value() + " offset " + consumerRecord.offset() + " fired.....");
            }
            break;
        }
    }

//    private void setOrderStatusFromFile() {
//        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
//            "\\Java school board\\board\\src\\main\\" +
//            "resources\\META-INF\\orderStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
//            String statusJSON = bufReader.readLine();
//            List<BoardOrderStatusDTO> statusList = FromJsonToDtoConverter.convertToOrderStatusList(statusJSON);
//            orderStatus=statusList;
//            socketService.broadcast(statusJSON);
//
//
//        }catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("JSON from file reading problems - orderStatus");
//        }
//
//    }

//    private void setDriverStatusFromFile(){
//        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
//                "\\Java school board\\board\\src\\main\\" +
//                "resources\\META-INF\\driverStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
//            String statusJSON = bufReader.readLine();
//            DriverStatusDTO lastDriverStatus = FromJsonToDtoConverter.convertToDriverStatusDto(statusJSON);
////            driverStatus = lastDriverStatus;
//            socketService.broadcast(statusJSON);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("JSON from file reading problems - driverStatus");
//        }
//
//    }

//    private void setTruckStatusFromFile(){
//        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
//                "\\Java school board\\board\\src\\main\\" +
//                "resources\\META-INF\\truckStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
//
//            String statusJSON = bufReader.readLine();
//            TruckStatusDTO lastTruckStatus = FromJsonToDtoConverter.convertToTruckStatusDto(statusJSON);
//            truckStatus = lastTruckStatus;
//            socketService.broadcast(statusJSON);
//        }catch (IOException e) {
//            throw new RuntimeException("JSON from file reading problems - truckStatus");
//        }
//    }

//    private DriverStatusDTO getLastDriverStatus(){
//        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
//                "\\Java school board\\board\\src\\main\\" +
//                "resources\\META-INF\\driverStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
//            String statusJSON = bufReader.readLine();
//            DriverStatusDTO lastDriverStatus = FromJsonToDtoConverter.convertToDriverStatusDto(statusJSON);
//            return lastDriverStatus;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("JSON from file reading problems");
//        }
//    }
//
    private TruckStatusDTO getLastTruckStatus(){
        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
                "\\Java school board\\board\\src\\main\\" +
                "resources\\META-INF\\truckStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){

            String statusJSON = bufReader.readLine();
            TruckStatusDTO lastTruckStatus = FromJsonToDtoConverter.convertToTruckStatusDto(statusJSON);
            return lastTruckStatus;
        }catch (IOException e) {
            throw new RuntimeException("JSON from file reading problems");
        }
    }


//    private List<BoardOrderStatusDTO> getLastOrderStatus(){
//        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
//                "\\Java school board\\board\\src\\main\\" +
//                "resources\\META-INF\\orderStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
//            String statusJSON = bufReader.readLine();
//            List<BoardOrderStatusDTO>lastOrderStatus = FromJsonToDtoConverter.convertToOrderStatusList(statusJSON);
//            return lastOrderStatus;
//
//
//        }catch (IOException e) {
//            throw new RuntimeException("JSON from file reading problems");
//        }
//    }

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
