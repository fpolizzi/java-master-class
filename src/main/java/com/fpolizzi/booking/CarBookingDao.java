package com.fpolizzi.booking;

/**
 * Created by fpolizzi on 31.12.25
 */
public class CarBookingDao {

    private static CarBooking[] carBookings;

    static {
        carBookings = new CarBooking[10];
    }

    public CarBooking[] getCarBookings() {
        return carBookings;
    }

    public void saveBooking(CarBooking carBooking) {
        int nextFreeIndex = -1;

        for (int i = 0; i < carBookings.length; i++) {
            if (carBookings[i] == null) {
                nextFreeIndex = i;
                break;
            }
        }

        if (nextFreeIndex > -1) {
            carBookings[nextFreeIndex] = carBooking;
            return;
        }

        // full array
        // copy all bookings to a new array with bigger space
        CarBooking[] biggerCarBookings = new CarBooking[carBookings.length + 10];

        for (int i = 0; i < carBookings.length; i++) {
            biggerCarBookings[i] = carBookings[i];
        }

        // add finally add new booking
        biggerCarBookings[carBookings.length] = carBooking;
        carBookings = biggerCarBookings;
    }
}
