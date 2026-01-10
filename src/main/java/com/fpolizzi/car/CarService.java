package com.fpolizzi.car;

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

    public Car[] getAllCars() {
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

    public Car[] getAllElectricCars() {
        int electricCarsCount = 0;

        Car[] cars = getAllCars();

        if (cars.length == 0) {
            return new Car[0];
        }

        for (Car car : cars) {
            if (car.isElectric()) {
                electricCarsCount++;
            }
        }

        if (electricCarsCount == 0) {
            return new Car[0];
        }

        Car[] electricCars = new Car[electricCarsCount];

        int index = 0;

        for (Car car : cars) {
            if (car.isElectric()) {
                electricCars[index++] = car;
            }
        }

        return electricCars;
    }
}
