package com.polizzi;
// DONE 1. create a new branch called initial-implementation
// DONE 2. create a package with your name. i.e., com.franco and move this file inside the new package
// DONE 3. implement https://amigoscode.com/learn/java-cli-build/lectures/3a83ecf3-e837-4ae5-85a8-f8ae3f60f7f05

import com.polizzi.booking.CarBooking;
import com.polizzi.booking.CarBookingService;
import com.polizzi.car.Car;
import com.polizzi.user.User;
import com.polizzi.user.UserService;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        UserService userService = new UserService();
        CarBookingService carBookingService = new CarBookingService();

        Scanner scanner = new Scanner(System.in);

        boolean keepLooping = true;

        while (keepLooping) {
            try {
                displayMenu();
                String choice = scanner.nextLine();
                switch (Integer.parseInt(choice)) {
                    case 1 -> bookCar(userService, carBookingService, scanner);
                    case 2 -> displayAllUserBookedCars(userService, carBookingService, scanner);
                    case 3 -> allBookings(carBookingService);
                    case 4 -> displayAvailableCars(carBookingService, false);
                    case 5 -> displayAvailableCars(carBookingService, true);
                    case 6 -> displayAllUsers(userService);
                    case 7 -> keepLooping = false;
                    default -> System.out.println(choice + " not a valid option ❌");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void allBookings(CarBookingService carBookingService) {
        CarBooking[] bookings = carBookingService.getBookings();
        if (bookings.length == 0) {
            System.out.println("No bookings available 😕");
            return;
        }
        for (CarBooking booking : bookings) {
            System.out.println("booking = " + booking);
        }
    }

    private static void displayAllUsers(UserService userService) {
        User[] users = userService.getUsers();
        if (users.length == 0) {
            System.out.println("❌ No users in the system");
            return;
        }
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void displayAvailableCars(CarBookingService carBookingService, boolean isElectric) {
        Car[] availableCars = isElectric ? carBookingService.getAvailableElectricCars() : carBookingService.getAvailableCars();
        if (availableCars.length == 0) {
            System.out.println("❌ No cars available for renting");
            return;
        }
        for (Car availableCar : availableCars) {
            System.out.println(availableCar);
        }
    }

    private static void displayAllUserBookedCars(UserService userService,
                                                 CarBookingService carBookingService,
                                                 Scanner scanner) {
        displayAllUsers(userService);

        System.out.println("➡️ select user id");
        String userId = scanner.nextLine();

        User user = userService.getUserById(UUID.fromString(userId));
        if (user == null) {
            System.out.println("❌ No user found with id " + userId);
            return;
        }

        Car[] userBookedCars = carBookingService.getUserBookedCars(user.getId());
        if (userBookedCars.length == 0) {
            System.out.printf("❌ user %s has no cars booked", user);
            return;
        }
        for (Car userBookedCar : userBookedCars) {
            System.out.println(userBookedCar);
        }
    }

    private static void bookCar(UserService userService, CarBookingService carBookingService, Scanner scanner) {
        displayAvailableCars(carBookingService, false);

        System.out.println("➡️ select car reg number");
        String regNumber = scanner.nextLine();

        displayAllUsers(userService);

        System.out.println("➡️ select user id");
        String userId = scanner.nextLine();

        try {
            User user = userService.getUserById(UUID.fromString(userId));
            if (user == null) {
                System.out.println("❌ No user found with id " + userId);
            } else {
                UUID bookingId = carBookingService.bookCar(user, regNumber);
                String confirmationMessage = """
                        🎉 Successfully booked car with reg number %s for user %s
                        Booking ref: %s
                        """.formatted(regNumber, user, bookingId);
                System.out.println(confirmationMessage);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayMenu() {
        System.out.println("""
                \n
                1 - Book Car
                2 - View All User Booked Cars
                3 - View All Bookings
                4 - View Available Cars
                5 - View Available Electric Cars
                6 - View all users
                7 - Exit
                """);
    }
}
