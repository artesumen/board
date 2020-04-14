package converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DriverDTO;

import java.io.IOException;

public class FromJsonToDtoConverter {
    public static DriverDTO convertToDriverDto(String driverJSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DriverDTO driverDTO = mapper.readValue(driverJSON,DriverDTO.class);
        return driverDTO;
    }
}
