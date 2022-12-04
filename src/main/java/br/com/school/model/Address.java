package br.com.school.model;

import java.util.List;

public class Address {
    private String street;
    private List<Double> coordinates;
    private String type = "Point";

    public Address() { }

    public Address(String street, List<Double> coordinates) {
        this.street = street;
        this.coordinates = coordinates;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }
}
