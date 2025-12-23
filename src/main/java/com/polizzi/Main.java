package com.polizzi;
// DONE 1. create a new branch called initial-implementation
// DONE 2. create a package with your name. i.e com.franco and move this file inside the new package
// TODO 3. implement https://amigoscode.com/learn/java-cli-build/lectures/3a83ecf3-e837-4ae5-85a8-f8ae3f60f7f5

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nJava Master Class - Car Booking System\n");

        while (true) {
            displayMainMenu();

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    // TODO: Implement book car functionality
                    break;
                case 2:
                    // TODO: Implement view all user booked cars functionality
                    break;
                case 3:
                    // TODO: Implement view all bookings functionality
                    break;
                case 4:
                    // TODO: Implement view available cars functionality
                    break;
                case 5:
                    // TODO: Implement view available electric cars functionality
                    break;
                case 6:
                    // TOOD: Implement view all users functionality
                    break;
                case 7:
                    System.out.println("\nThank you for using Car Booking System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("\nInvalid choice! Use an integer from 1 to 7.\n");
            }
        }
    }

    public static void displayMainMenu() {

        System.out.println(" 1 - Book Car");
        System.out.println(" 2 - View All User Booked Cars");
        System.out.println(" 3 - View All Bookings");
        System.out.println(" 4 - View Available Cars");
        System.out.println(" 5 - View Available Electric Cars");
        System.out.println(" 6 - View all users");
        System.out.println(" 7 - Exit");
    }

    public static int getUserChoice(Scanner scanner) {

        System.out.print("\nEnter your choice: ");
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        }

        scanner.next();

        return 0;
    }
}
