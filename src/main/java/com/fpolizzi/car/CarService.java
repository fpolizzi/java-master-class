package com.fpolizzi.car;

import java.util.ArrayList;
import java.util.List;

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

    public Car getCar(String registrationNumber) {
        for (Car car : getAllCars()) {
            if (registrationNumber.equals(car.getRegistrationNumber())) {
                return car;
            }
        }
        return null;
    }

    public List<Car> getAllElectricCars() {
        List<Car> electricCars = new ArrayList<>();

        for (Car car : getAllCars()) {
            if (car.isElectric()) {
                electricCars.add(car);
            }
        }

        return electricCars;
    }
}
