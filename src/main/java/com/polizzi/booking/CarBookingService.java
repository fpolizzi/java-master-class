package com.polizzi.booking;

import com.polizzi.car.Car;
import com.polizzi.car.CarService;
import com.polizzi.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by fpolizzi on 31.12.25
 */
public class CarBookingService {
    private final CarBookingDao carBookingDao = new CarBookingDao();
    private final CarService carService = new CarService();

    public UUID bookCar(User user, String registrationNumber) {
        Car[] availableCars = getAvailableCars();

        if (availableCars.length == 0) {
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

    public Car[] getUserBookedCars(UUID userId) {
        CarBooking[] carBookings = carBookingDao.getCarBookings();

        int numberOfBookingsForUser = 0;

        for (CarBooking cb : carBookings) {
            if (cb != null && cb.getUser().getId().equals(userId)) {
                ++numberOfBookingsForUser;
            }
        }

        if (numberOfBookingsForUser == 0) {
            return new Car[0];
        }

        Car[] userCars = new Car[numberOfBookingsForUser];

        int index = 0;
        for (CarBooking carBooking : carBookings) {
            if (carBooking != null && carBooking.getUser().getId().equals(userId)) {
                userCars[index++] = carBooking.getCar();
            }
        }
        return userCars;
    }


    public Car[] getAvailableCars() {
        return getCars(carService.getAllCars());
    }

    public Car[] getAvailableElectricCars() {
        return getCars(carService.getAllElectricCars());
    }

    private Car[] getCars(Car[] cars) {

        // no cars in the system yet
        if (cars.length == 0) {
            return new Car[0];
        }

        CarBooking[] carBookings = carBookingDao.getCarBookings();

        // no bookings yet, so all cars are available
        if (carBookings.length == 0) {
            return cars;
        }

        int availableCarsCount = 0;
        for (Car car : cars) {
            boolean booked = false;
            for (CarBooking carBooking : carBookings) {
                if (carBooking != null && carBooking.getCar().equals(car)) {
                    booked = true;
                    break;
                }
            }
            if (!booked) {
                availableCarsCount++;
            }
        }

        Car[] availableCars = new Car[availableCarsCount];
        int index = 0;

        for (Car car : cars) {
            boolean booked = false;
            for (CarBooking carBooking : carBookings) {
                if (carBooking != null && carBooking.getCar().equals(car)) {
                    booked = true;
                    break;
                }
            }
            if (!booked) {
                availableCars[index++] = car;
            }
        }

        return availableCars;
    }

    public CarBooking[] getBookings() {
        CarBooking[] carBookings = carBookingDao.getCarBookings();

        int numberOfBookings = 0;

        for (CarBooking cb : carBookings) {
            if (cb != null) {
                ++numberOfBookings;
            }
        }

        if (numberOfBookings == 0) {
            return new CarBooking[0];
        }

        CarBooking[] bookings = new CarBooking[numberOfBookings];

        int index = 0;
        for (CarBooking carBooking : carBookings) {
            if (carBooking != null) {
                bookings[index++] = carBooking;
            }
        }
        return bookings;
    }
}
