package service;

import converter.FromJsonToDtoConverter;
import dto.BoardOrderStatusDTO;
import dto.DriverStatusDTO;
import dto.TruckStatusDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

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

    private List<BoardOrderStatusDTO> orderStatus;

    private DriverStatusDTO driverStatus;

    private TruckStatusDTO truckStatus;

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
                if (consumerRecord.value().contains("totalDrivers")) {
                    try (FileWriter writer = new FileWriter("C:\\Users\\Admin\\Desktop\\Java" +
                            "\\Java school board\\board\\src\\main\\" +
                            "resources\\META-INF\\driverStatus.txt")) {
                        writer.write(consumerRecord.value());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setDriverStatusFromFile();

                } else if (consumerRecord.value().contains("totalTrucksNumber")) {

                    try (FileWriter writer = new FileWriter("C:\\Users\\Admin\\Desktop\\Java" +
                            "\\Java school board\\board\\src\\main\\" +
                            "resources\\META-INF\\truckStatus.txt")) {
                        writer.write(consumerRecord.value());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setTruckStatusFromFile();
                } else if(consumerRecord.value().contains("orderId")) {
                    try(FileWriter writer = new FileWriter("C:\\Users\\Admin\\Desktop\\Java" +
                            "\\Java school board\\board\\src\\main\\" +
                            "resources\\META-INF\\orderStatus.txt")){
                        writer.write(consumerRecord.value());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setOrderStatusFromFile();

                }
                System.out.println("Event  " + consumerRecord.value() + " offset " + consumerRecord.offset() + " fired.....");
            }
            break;
        }
    }

    private void setOrderStatusFromFile() {
        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
            "\\Java school board\\board\\src\\main\\" +
            "resources\\META-INF\\orderStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
            String statusJSON = bufReader.readLine();
            List<BoardOrderStatusDTO> statusList = FromJsonToDtoConverter.convertToOrderStatusList(statusJSON);
            orderStatus=statusList;
            socketService.broadcast(statusJSON);


        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON from file reading problems - orderStatus");
        }

    }

    private void setDriverStatusFromFile(){
        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
                "\\Java school board\\board\\src\\main\\" +
                "resources\\META-INF\\driverStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
            String statusJSON = bufReader.readLine();
            DriverStatusDTO lastDriverStatus = FromJsonToDtoConverter.convertToDriverStatusDto(statusJSON);
            driverStatus = lastDriverStatus;
            socketService.broadcast(statusJSON);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON from file reading problems - driverStatus");
        }

    }

    private void setTruckStatusFromFile(){
        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
                "\\Java school board\\board\\src\\main\\" +
                "resources\\META-INF\\truckStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){

            String statusJSON = bufReader.readLine();
            TruckStatusDTO lastTruckStatus = FromJsonToDtoConverter.convertToTruckStatusDto(statusJSON);
            truckStatus = lastTruckStatus;
            socketService.broadcast(statusJSON);
        }catch (IOException e) {
            throw new RuntimeException("JSON from file reading problems - truckStatus");
        }
    }

    private DriverStatusDTO getLastDriverStatus(){
        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
                "\\Java school board\\board\\src\\main\\" +
                "resources\\META-INF\\driverStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
            String statusJSON = bufReader.readLine();
            DriverStatusDTO lastDriverStatus = FromJsonToDtoConverter.convertToDriverStatusDto(statusJSON);
            return lastDriverStatus;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON from file reading problems");
        }
    }

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


    private List<BoardOrderStatusDTO> getLastOrderStatus(){
        try(FileReader reader = new FileReader("C:\\Users\\Admin\\Desktop\\Java" +
                "\\Java school board\\board\\src\\main\\" +
                "resources\\META-INF\\orderStatus.txt");BufferedReader bufReader = new BufferedReader(reader)){
            String statusJSON = bufReader.readLine();
            List<BoardOrderStatusDTO>lastOrderStatus = FromJsonToDtoConverter.convertToOrderStatusList(statusJSON);
            return lastOrderStatus;


        }catch (IOException e) {
            throw new RuntimeException("JSON from file reading problems");
        }
    }

    public DriverStatusDTO getDriverStatus() {
        if (driverStatus == null) {
            driverStatus = getLastDriverStatus();
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
            truckStatus = getLastTruckStatus();
        }
        return truckStatus;
    }

    public List<BoardOrderStatusDTO> getOrderStatus() {
        if(orderStatus == null){
            orderStatus = getLastOrderStatus();
        }
        return orderStatus;
    }
}
