//package service;
//
//
////import config.BoardKafkaConsumer;
//import config.KafkaMDB;
//import dto.DriverDTO;
//
//import javax.ejb.EJB;
//import javax.ejb.Stateless;
//import javax.enterprise.context.Dependent;
//import javax.inject.Inject;
//import javax.inject.Named;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//@Stateless
//@Named
//@Dependent
////@ManagedBean
////@ViewScoped
//public class DriverUpdateSessionBean implements DriverUpdateSessionRemote, Serializable {
////
////    @Inject
////    BoardKafkaConsumer consumer;
//
//    @Inject
//    KafkaMDB consumer;
//
////    public DriverUpdateSessionBean() {
////        consumer = new BoardKafkaConsumer();
////    }
//
//    @Override
//    public DriverDTO getSavedDriver(){
//        List<DriverDTO> driverList = new ArrayList();
//        DriverDTO driverJSONs = consumer.findMessageAndPass();
////        for (String JSON:driverJSONs) {
////            try {
////                driverList.add(FromJsonToDtoConverter.convertToDriverDto(JSON));
////            } catch (IOException e) {
////                throw new RuntimeException("Converting from JSON to POJO troubles");
////            }
////        }
//        return driverJSONs;
//    }
//
//
//
//
//}
