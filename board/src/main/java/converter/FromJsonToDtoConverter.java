package converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DriverStatusDTO;

import java.io.IOException;

public class FromJsonToDtoConverter {

    public static DriverStatusDTO convertToDriverStatusDto(String driverJSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DriverStatusDTO status = mapper.readValue(driverJSON,DriverStatusDTO.class);
        return status;
    }
}
