package com.fpolizzi.car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fpolizzi on 29.12.25
 */
public class CarDao {

    private static final List<Car> cars = new ArrayList<>();

    static {
        cars.add(new Car("12345", new BigDecimal("29.00"), CarBrand.FIAT, false));
        cars.add(new Car("23456", new BigDecimal("59.00"), CarBrand.RENAULT, true));
        cars.add(new Car("34567", new BigDecimal("79.00"), CarBrand.MERCEDES, false));
        cars.add(new Car("45678", new BigDecimal("89.00"), CarBrand.JAGUAR, false));
        cars.add(new Car("56789", new BigDecimal("49.00"), CarBrand.BYD, true));
        cars.add(new Car("67890", new BigDecimal("39.00"), CarBrand.AUDI, false));
    }

    public List<Car> getAllCars() {
        return cars;
    }
}
