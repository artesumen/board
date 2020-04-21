package converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import dto.BoardOrderStatusDTO;
import dto.DriverStatusDTO;
import dto.TruckStatusDTO;

import java.io.IOException;
import java.util.List;

public class FromJsonToDtoConverter {

    public static DriverStatusDTO convertToDriverStatusDto(String driverJSON){
        ObjectMapper mapper = new ObjectMapper();
        DriverStatusDTO status = null;
        try {
            status = mapper.readValue(driverJSON, DriverStatusDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("convertToDriverStatusDto IOException");
        }
        return status;
    }

    public static TruckStatusDTO convertToTruckStatusDto(String truckJSON){
        ObjectMapper mapper = new ObjectMapper();
        TruckStatusDTO status = null;
        try {
            status = mapper.readValue(truckJSON, TruckStatusDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("convertToTruckStatusDTO IOException");
        }
        return status;
    }

    public static List<BoardOrderStatusDTO> convertToOrderStatusList(String jsonArray){
        ObjectMapper mapper = new ObjectMapper();
        try {
            CollectionType javaType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, BoardOrderStatusDTO.class);
            List<BoardOrderStatusDTO> lastStatus = mapper.readValue(jsonArray,javaType);

            for(BoardOrderStatusDTO orderStatus:lastStatus){
                System.out.println("parsed : " + orderStatus.getDrivers().toString());
            }
            return lastStatus;
        } catch (IOException e) {
            throw new RuntimeException("convert to OrderStatusDTO IOException");
        }

    }
}
