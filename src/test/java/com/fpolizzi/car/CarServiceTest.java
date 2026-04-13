package com.fpolizzi.car;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CarServiceTest {

    private CarService carServiceWith(List<Car> cars) {
        CarDao fakeDao = new CarDao() {
            @Override
            public List<Car> getAllCars() {
                return cars;
            }
        };
        return new CarService(fakeDao);
    }

    @Test
    void getAllCars_shouldReturnAllCars() {
        List<Car> cars = List.of(
                new Car("ABC123", BigDecimal.valueOf(50), CarBrand.AUDI, false),
                new Car("XYZ789", BigDecimal.valueOf(80), CarBrand.BYD, true),
                new Car("DEF456", BigDecimal.valueOf(60), CarBrand.FIAT, false)
        );
        CarService underTest = carServiceWith(cars);

        List<Car> result = underTest.getAllCars();

        assertThat(result).hasSize(3);
    }

    @Test
    void getCar_shouldReturnOptionalWithCar_whenRegNumberMatches() {
        Car expected = new Car("ABC123", BigDecimal.valueOf(50), CarBrand.AUDI, false);
        CarService underTest = carServiceWith(List.of(
                expected,
                new Car("XYZ789", BigDecimal.valueOf(80), CarBrand.BYD, true)
        ));

        Optional<Car> result = underTest.getCar("ABC123");

        assertThat(result).contains(expected);
    }

    @Test
    void getCar_shouldReturnEmptyOptional_whenRegNumberNotFound() {
        CarService underTest = carServiceWith(List.of(
                new Car("ABC123", BigDecimal.valueOf(50), CarBrand.AUDI, false)
        ));

        Optional<Car> result = underTest.getCar("NOTFOUND");

        assertThat(result).isEmpty();
    }

    @Test
    void getAllElectricCars_shouldReturnOnlyElectricCars() {
        Car electric1 = new Car("EV001", BigDecimal.valueOf(90), CarBrand.BYD, true);
        Car electric2 = new Car("EV002", BigDecimal.valueOf(95), CarBrand.JAGUAR, true);
        Car nonElectric = new Car("GAS001", BigDecimal.valueOf(50), CarBrand.FIAT, false);
        CarService underTest = carServiceWith(List.of(electric1, electric2, nonElectric));

        List<Car> result = underTest.getAllElectricCars();

        assertThat(result).containsExactlyInAnyOrder(electric1, electric2);
    }

    @Test
    void getAllElectricCars_shouldReturnEmptyList_whenNoElectricCars() {
        CarService underTest = carServiceWith(List.of(
                new Car("GAS001", BigDecimal.valueOf(50), CarBrand.FIAT, false),
                new Car("GAS002", BigDecimal.valueOf(60), CarBrand.AUDI, false)
        ));

        List<Car> result = underTest.getAllElectricCars();

        assertThat(result).isEmpty();
    }

    @Test
    void getAllElectricCars_shouldReturnEmptyList_whenNoCars() {
        CarService underTest = carServiceWith(Collections.emptyList());

        List<Car> result = underTest.getAllElectricCars();

        assertThat(result).isEmpty();
    }
}
