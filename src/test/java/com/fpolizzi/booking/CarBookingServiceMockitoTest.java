package com.fpolizzi.booking;

import com.fpolizzi.car.Car;
import com.fpolizzi.car.CarBrand;
import com.fpolizzi.car.CarService;
import com.fpolizzi.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarBookingServiceMockitoTest {

    @Mock
    private CarBookingDao carBookingDao;

    @Mock
    private CarService carService;

    @InjectMocks
    private CarBookingService underTest;

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
        User user = user(UUID.randomUUID());
        Car available = car("ABC123", false);
        when(carService.getAllCars()).thenReturn(List.of(available));
        when(carBookingDao.getCarBookings()).thenReturn(List.of());
        when(carService.getCar("ABC123")).thenReturn(Optional.of(available));

        UUID bookingId = underTest.bookCar(user, "ABC123");

        ArgumentCaptor<CarBooking> captor = ArgumentCaptor.forClass(CarBooking.class);
        verify(carBookingDao).saveBooking(captor.capture());
        CarBooking saved = captor.getValue();
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getCar()).isEqualTo(available);
        assertThat(bookingId).isEqualTo(saved.getBookingId());
    }

    @Test
    void bookCar_throwsException_whenAllCarsAreAlreadyBooked() {
        Car car = car("ABC123", false);
        CarBooking existingBooking = new CarBooking(UUID.randomUUID(), user(UUID.randomUUID()), car, null);
        when(carService.getAllCars()).thenReturn(List.of(car));
        when(carBookingDao.getCarBookings()).thenReturn(List.of(existingBooking));

        assertThatThrownBy(() -> underTest.bookCar(user(UUID.randomUUID()), "ABC123"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No car available");
    }

    @Test
    void bookCar_throwsException_whenSpecificCarAlreadyBooked() {
        Car available = car("FREE01", false);
        Car booked = car("TAKEN1", false);
        CarBooking existingBooking = new CarBooking(UUID.randomUUID(), user(UUID.randomUUID()), booked, null);
        when(carService.getAllCars()).thenReturn(List.of(available, booked));
        when(carBookingDao.getCarBookings()).thenReturn(List.of(existingBooking));

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
        when(carService.getAllCars()).thenReturn(List.of(car1, car2, car3));
        when(carBookingDao.getCarBookings()).thenReturn(List.of(booking));

        List<Car> result = underTest.getAvailableCars();

        assertThat(result).containsExactlyInAnyOrder(car2, car3);
    }

    @Test
    void getAvailableCars_shouldReturnAllCars_whenNoneAreBooked() {
        Car car1 = car("CAR001", false);
        Car car2 = car("CAR002", false);
        when(carService.getAllCars()).thenReturn(List.of(car1, car2));
        when(carBookingDao.getCarBookings()).thenReturn(List.of());

        List<Car> result = underTest.getAvailableCars();

        assertThat(result).containsExactlyInAnyOrder(car1, car2);
    }

    @Test
    void getAvailableElectricCars_shouldReturnOnlyUnbookedElectricCars() {
        Car electric = car("EV001", true);
        Car electricBooked = car("EV002", true);
        CarBooking booking = new CarBooking(UUID.randomUUID(), user(UUID.randomUUID()), electricBooked, null);
        when(carService.getAllElectricCars()).thenReturn(List.of(electric, electricBooked));
        when(carBookingDao.getCarBookings()).thenReturn(List.of(booking));

        List<Car> result = underTest.getAvailableElectricCars();

        assertThat(result).containsExactly(electric);
    }

    @Test
    void getUserBookedCars_shouldReturnOnlyCarsBookedByThatUser() {
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
        when(carBookingDao.getCarBookings()).thenReturn(bookings);

        List<Car> result = underTest.getUserBookedCars(userId);

        assertThat(result).containsExactlyInAnyOrder(car1, car2);
    }

    @Test
    void getUserBookedCars_shouldReturnEmptyList_whenUserHasNoBookings() {
        when(carBookingDao.getCarBookings()).thenReturn(List.of());

        List<Car> result = underTest.getUserBookedCars(UUID.randomUUID());

        assertThat(result).isEmpty();
    }
}
