package com.fpolizzi.booking;

import com.fpolizzi.car.Car;
import com.fpolizzi.car.CarService;
import com.fpolizzi.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by fpolizzi on 31.12.25
 */
public class CarBookingService {
    // dependencies
    private final CarBookingDao carBookingDao;
    private final CarService carService;

    // inject dependencies
    public CarBookingService(CarBookingDao carBookingDao, CarService carService) {
        this.carBookingDao = carBookingDao;
        this.carService = carService;
    }

    public UUID bookCar(User user, String registrationNumber) {
        List<Car> availableCars = getAvailableCars();

        if (availableCars.isEmpty()) {
            throw new IllegalStateException("No car available for renting");
        }

        boolean carAvailable = availableCars.stream()
                .anyMatch(car -> car.getRegistrationNumber().equals(registrationNumber));

        if (!carAvailable) {
            throw new IllegalStateException("Already booked. car with regNumber " + registrationNumber);
        }

        Optional<Car> car = carService.getCar(registrationNumber);
        UUID bookingId = UUID.randomUUID();
        carBookingDao.saveBooking(new CarBooking(bookingId, user, car, LocalDateTime.now()));
        return bookingId;
    }

    public List<Car> getUserBookedCars(UUID userId) {
        return carBookingDao.getCarBookings().stream()
                .filter(booking -> booking.getUser().getId().equals(userId))
                .map(CarBooking::getCar)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public List<Car> getAvailableCars() {
        return getCars(carService.getAllCars());
    }

    public List<Car> getAvailableElectricCars() {
        return getCars(carService.getAllElectricCars());
    }

    private List<Car> getCars(List<Car> cars) {
        Set<Car> bookedCars = carBookingDao.getCarBookings().stream()
                .map(CarBooking::getCar)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return cars.stream()
                .filter(car -> !bookedCars.contains(car))
                .collect(Collectors.toList());
    }

    public List<CarBooking> getBookings() {
        return carBookingDao.getCarBookings().stream().collect(Collectors.toList());
    }
}
