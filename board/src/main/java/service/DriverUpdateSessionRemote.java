package service;

import dto.DriverDTO;

import javax.ejb.Remote;

@Remote
public interface DriverUpdateSessionRemote{
    DriverDTO getSavedDriver();
}
