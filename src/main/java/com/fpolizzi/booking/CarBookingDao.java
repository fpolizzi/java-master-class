package com.fpolizzi.booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fpolizzi on 31.12.25
 */
public class CarBookingDao {

    private static final List<CarBooking> carBookings = new ArrayList<>();

    public List<CarBooking> getCarBookings() {
        return carBookings;
    }

    public void saveBooking(CarBooking carBooking) {
        carBookings.add(carBooking);
    }
}
