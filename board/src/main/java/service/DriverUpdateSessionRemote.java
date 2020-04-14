package service;

import dto.DriverDTO;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DriverUpdateSessionRemote{
    List<DriverDTO> getSavedDriver();
}
