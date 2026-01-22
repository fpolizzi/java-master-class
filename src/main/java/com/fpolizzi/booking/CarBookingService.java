package com.fpolizzi.booking;

import com.fpolizzi.car.Car;
import com.fpolizzi.car.CarService;
import com.fpolizzi.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        for (Car availableCar : availableCars) {
            // let's make sure the car user wants still available
            if (availableCar.getRegistrationNumber().equals(registrationNumber)) {
                Car car = carService.getCar(registrationNumber);
                UUID bookingId = UUID.randomUUID();
                carBookingDao.saveBooking(
                        new CarBooking(bookingId, user, car, LocalDateTime.now())
                );
                // at this point we are done therefore, we can exit this method
                return bookingId;
            }
        }
        throw new IllegalStateException("Already booked. car with regNumber " + registrationNumber);
    }

    public List<Car> getUserBookedCars(UUID userId) {
        List<Car> userCars = new ArrayList<>();

        for (CarBooking carBooking : carBookingDao.getCarBookings()) {
            if (carBooking.getUser().getId().equals(userId)) {
                userCars.add(carBooking.getCar());
            }
        }

        return userCars;
    }

    public List<Car> getAvailableCars() {
        return getCars(carService.getAllCars());
    }

    public List<Car> getAvailableElectricCars() {
        return getCars(carService.getAllElectricCars());
    }

    private List<Car> getCars(List<Car> cars) {
        // no cars in the system yet
        if (cars.isEmpty()) {
            return new ArrayList<>();
        }

        List<CarBooking> carBookings = carBookingDao.getCarBookings();

        // no bookings yet, so all cars are available
        if (carBookings.isEmpty()) {
            return new ArrayList<>(cars);
        }

        List<Car> availableCars = new ArrayList<>();

        for (Car car : cars) {
            boolean booked = false;
            for (CarBooking carBooking : carBookings) {
                if (carBooking.getCar().equals(car)) {
                    booked = true;
                    break;
                }
            }
            if (!booked) {
                availableCars.add(car);
            }
        }

        return availableCars;
    }

    public List<CarBooking> getBookings() {
        return new ArrayList<>(carBookingDao.getCarBookings());
    }
}
