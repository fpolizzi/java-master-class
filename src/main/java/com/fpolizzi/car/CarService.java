package com.fpolizzi.car;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by fpolizzi on 29.12.25
 */
public class CarService {
    // dependency
    private final CarDao carDao;

    // inject dependency
    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> getAllCars() {
        return carDao.getAllCars();
    }

    public Optional<Car> getCar(String registrationNumber) {
        return getAllCars()
                .stream()
                .filter(car -> registrationNumber.equals(car.getRegistrationNumber()))
                .findFirst();
    }

    public List<Car> getAllElectricCars() {
        return getAllCars()
                .stream()
                .filter(car -> car.isElectric())
                .collect(Collectors.toList());
    }
}
