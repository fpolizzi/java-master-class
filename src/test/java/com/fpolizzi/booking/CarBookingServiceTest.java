package com.fpolizzi.booking;

import com.fpolizzi.car.Car;
import com.fpolizzi.car.CarBrand;
import com.fpolizzi.car.CarDao;
import com.fpolizzi.car.CarService;
import com.fpolizzi.user.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CarBookingServiceTest {

    // --- Test doubles ---

    private static CarService carServiceWith(List<Car> cars) {
        return new CarService(new CarDao() {
            @Override
            public List<Car> getAllCars() {
                return cars;
            }
        });
    }

    private static class FakeCarBookingDao extends CarBookingDao {
        private final List<CarBooking> bookings = new ArrayList<>();
        private final List<CarBooking> savedBookings = new ArrayList<>();

        FakeCarBookingDao(List<CarBooking> existingBookings) {
            bookings.addAll(existingBookings);
        }

        @Override
        public List<CarBooking> getCarBookings() {
            return bookings;
        }

        @Override
        public void saveBooking(CarBooking carBooking) {
            bookings.add(carBooking);
            savedBookings.add(carBooking);
        }
    }

    // --- Helpers ---

    private static Car car(String reg, boolean electric) {
        return new Car(reg, BigDecimal.valueOf(50), CarBrand.AUDI, electric);
    }

    private static User user(UUID id) {
        return new User(id, "John", "Doe");
    }

    // --- Tests ---

    @Test
    void bookCar_shouldReturnBookingId_whenCarIsAvailable() {
        Car available = car("ABC123", false);
        FakeCarBookingDao dao = new FakeCarBookingDao(List.of());
        CarBookingService underTest = new CarBookingService(dao, carServiceWith(List.of(available)));

        UUID bookingId = underTest.bookCar(user(UUID.randomUUID()), "ABC123");

        assertThat(bookingId).isNotNull();
        assertThat(dao.savedBookings).hasSize(1);
    }

    @Test
    void bookCar_throwsException_whenNoAvailableCars() {
        Car car = car("ABC123", false);
        User booker = user(UUID.randomUUID());
        CarBooking existingBooking = new CarBooking(UUID.randomUUID(), booker, car, null);
        FakeCarBookingDao dao = new FakeCarBookingDao(List.of(existingBooking));
        CarBookingService underTest = new CarBookingService(dao, carServiceWith(List.of(car)));

        assertThatThrownBy(() -> underTest.bookCar(user(UUID.randomUUID()), "ABC123"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No car available");
    }

    @Test
    void bookCar_throwsException_whenSpecificCarAlreadyBooked() {
        Car available = car("FREE01", false);
        Car booked = car("TAKEN1", false);
        User booker = user(UUID.randomUUID());
        CarBooking existingBooking = new CarBooking(UUID.randomUUID(), booker, booked, null);
        FakeCarBookingDao dao = new FakeCarBookingDao(List.of(existingBooking));
        CarBookingService underTest = new CarBookingService(dao, carServiceWith(List.of(available, booked)));

        assertThatThrownBy(() -> underTest.bookCar(user(UUID.randomUUID()), "TAKEN1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Already booked");
    }

    @Test
    void getAvailableCars_shouldReturnOnlyUnbookedCars() {
        Car car1 = car("CAR001", false);
        Car car2 = car("CAR002", false);
        Car car3 = car("CAR003", false);
        CarBooking booking = new CarBooking(UUID.randomUUID(), user(UUID.randomUUID()), car1, null);
        FakeCarBookingDao dao = new FakeCarBookingDao(List.of(booking));
        CarBookingService underTest = new CarBookingService(dao, carServiceWith(List.of(car1, car2, car3)));

        List<Car> result = underTest.getAvailableCars();

        assertThat(result).containsExactlyInAnyOrder(car2, car3);
    }

    @Test
    void getAvailableCars_shouldReturnAllCars_whenNoneAreBooked() {
        Car car1 = car("CAR001", false);
        Car car2 = car("CAR002", false);
        FakeCarBookingDao dao = new FakeCarBookingDao(List.of());
        CarBookingService underTest = new CarBookingService(dao, carServiceWith(List.of(car1, car2)));

        List<Car> result = underTest.getAvailableCars();

        assertThat(result).containsExactlyInAnyOrder(car1, car2);
    }

    @Test
    void getAvailableElectricCars_shouldReturnOnlyUnbookedElectricCars() {
        Car electric = car("EV001", true);
        Car electricBooked = car("EV002", true);
        Car gasAvailable = car("GAS01", false);
        CarBooking booking = new CarBooking(UUID.randomUUID(), user(UUID.randomUUID()), electricBooked, null);
        FakeCarBookingDao dao = new FakeCarBookingDao(List.of(booking));
        CarBookingService underTest = new CarBookingService(dao, carServiceWith(List.of(electric, electricBooked, gasAvailable)));

        List<Car> result = underTest.getAvailableElectricCars();

        assertThat(result).containsExactly(electric);
    }

    @Test
    void getUserBookedCars_shouldReturnOnlyCarsBooedByThatUser() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        Car car1 = car("CAR001", false);
        Car car2 = car("CAR002", false);
        Car car3 = car("CAR003", false);
        List<CarBooking> bookings = List.of(
                new CarBooking(UUID.randomUUID(), user(userId), car1, null),
                new CarBooking(UUID.randomUUID(), user(userId), car2, null),
                new CarBooking(UUID.randomUUID(), user(otherUserId), car3, null)
        );
        FakeCarBookingDao dao = new FakeCarBookingDao(bookings);
        CarBookingService underTest = new CarBookingService(dao, carServiceWith(List.of(car1, car2, car3)));

        List<Car> result = underTest.getUserBookedCars(userId);

        assertThat(result).containsExactlyInAnyOrder(car1, car2);
    }

    @Test
    void getUserBookedCars_shouldReturnEmptyList_whenUserHasNoBookings() {
        FakeCarBookingDao dao = new FakeCarBookingDao(List.of());
        CarBookingService underTest = new CarBookingService(dao, carServiceWith(List.of()));

        List<Car> result = underTest.getUserBookedCars(UUID.randomUUID());

        assertThat(result).isEmpty();
    }
}
