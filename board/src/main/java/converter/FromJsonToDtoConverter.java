package converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DriverStatusDTO;
import dto.TruckStatusDTO;

import java.io.IOException;

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


}
