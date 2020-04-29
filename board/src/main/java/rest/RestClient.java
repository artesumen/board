package rest;

import dto.BoardOrderStatusDTO;
import dto.DriverStatusDTO;
import dto.TruckStatusDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;

import javax.ejb.Stateless;
import java.util.List;


@Stateless
public class RestClient {
    private static final String DRIVER_URI = "http://localhost:8085/rest/driverStatus";

    private static final String TRUCK_URI = "http://localhost:8085/rest/truckStatus";
    private static final String ORDER_URI = "http://localhost:8085/rest/orderStatus";

    public RestClient() {
    }

    private Client client = ClientBuilder.newClient();

    public DriverStatusDTO getDriverStatus() {
        return client
                .target(DRIVER_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(DriverStatusDTO.class);
    }

    public TruckStatusDTO getTruckStatus() {
        return client
                .target(TRUCK_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(TruckStatusDTO.class);
    }

    public List<BoardOrderStatusDTO> getOrderStatus() {
        return client
                .target(ORDER_URI)
                .request(MediaType.APPLICATION_JSON)
                .get(List.class);
    }



}
