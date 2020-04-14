package controller;

import dto.DriverDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.DriverUpdateSessionBean;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Named(value = "driverController")
@SessionScoped
public class DriverController implements Serializable {


    private DriverDTO driver = new DriverDTO();
    private int driverId;
    private String driverFirstName;
    private String driverSurname;
    private Integer driverPrivateNum;
    private Integer driverWorkedHours;
    private DriverDTO.Status driverStatus;
    private Integer driverCityId;
    private Integer driversTruckId;
    private Integer userId;

    @EJB
    DriverUpdateSessionBean driverUpdateSessionBean;

//    public List<DriverDTO> getSavedDriver(){
//
//        return driverUpdateSessionBean.getSavedDriver();
//    }

    public String viewDrivers(){
        driverUpdateSessionBean.getSavedDriver();
        return "home.xhtml?faces-redirect=true";
    }

}
