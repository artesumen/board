package service;

import dto.DriverDTO;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface DriverUpdateSessionRemote{
    List<DriverDTO> getSavedDriver();
}
