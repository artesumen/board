package dto;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.*;

public class DriverDTO {

    private int driverId;


    private String driverFirstName;


    private String driverSurname;


    private Integer driverPrivateNum;

    private Integer driverWorkedHours;



    private Status driverStatus;

    public enum Status {
        REST,CARGO_HANDLING, REST_ON_SHIFT,DRIVING,CO_DRIVER;}


    private Integer driverCityId;

    private Integer driversTruckId;

    private Integer userId;

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverSurname() {
        return driverSurname;
    }

    public void setDriverSurname(String driverSurname) {
        this.driverSurname = driverSurname;
    }

    public Integer getDriverPrivateNum() {
        return driverPrivateNum;
    }

    public void setDriverPrivateNum(Integer driverPrivateNum) {
        this.driverPrivateNum = driverPrivateNum;
    }

    public Integer getDriverWorkedHours() {
        return driverWorkedHours;
    }

    public void setDriverWorkedHours(Integer driverWorkedHours) {
        this.driverWorkedHours = driverWorkedHours;
    }

    public Status getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(Status driverStatus) {
        this.driverStatus = driverStatus;
    }

    public Integer getDriverCityId() {
        return driverCityId;
    }

    public void setDriverCityId(Integer driverCityId) {
        this.driverCityId = driverCityId;
    }

    public Integer getDriversTruckId() {
        return driversTruckId;
    }

    public void setDriversTruckId(Integer driversTruckId) {
        this.driversTruckId = driversTruckId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
