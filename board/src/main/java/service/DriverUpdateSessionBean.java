package service;


import config.BoardKafkaConsumer;
import converter.FromJsonToDtoConverter;
import dto.DriverDTO;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



@Stateless
@Named
@Dependent
@ManagedBean
public class DriverUpdateSessionBean implements DriverUpdateSessionRemote, Serializable {

    BoardKafkaConsumer consumer;

    public DriverUpdateSessionBean() {
        consumer = new BoardKafkaConsumer();
    }

    @Override
    public List<DriverDTO> getSavedDriver(){
        List<DriverDTO> driverList = new ArrayList();
        List<String> driverJSONs = consumer.runConsumerAndGetMsg();
        for (String JSON:driverJSONs) {
            try {
                driverList.add(FromJsonToDtoConverter.convertToDriverDto(JSON));
            } catch (IOException e) {
                throw new RuntimeException("Converting from JSON to POJO troubles");
            }
        }
        return driverList;

    }




}
