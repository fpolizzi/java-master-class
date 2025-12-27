package com.polizzi.car;

/**
 * Created by fpolizzi on 27.12.25
 */
import java.math.BigDecimal;
import java.util.Objects;

public class Car {
    private String registrationNumber;
    private BigDecimal rentalPricePerDay;

    private boolean isElectric;

    public Car(String registrationNumber, BigDecimal rentalPricePerDay, boolean isElectric) {
        this.registrationNumber = registrationNumber;
        this.rentalPricePerDay = rentalPricePerDay;
        this.isElectric = isElectric;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public BigDecimal getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public void setRentalPricePerDay(BigDecimal rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }

    @Override
    public String toString() {
        return "Car{" +
                "registrationNumber='" + registrationNumber + '\'' +
                ", rentalPricePerDay=" + rentalPricePerDay +
                ", isElectric=" + isElectric +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return isElectric == car.isElectric &&
                Objects.equals(registrationNumber, car.registrationNumber) &&
                Objects.equals(rentalPricePerDay, car.rentalPricePerDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber, rentalPricePerDay, isElectric);
    }
}