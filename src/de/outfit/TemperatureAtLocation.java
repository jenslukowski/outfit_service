package de.outfit;

public class TemperatureAtLocation {
    private final String city;
    private final String country;
    private final Temperature temperature;

    public TemperatureAtLocation(String city, String country, Temperature temperature) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Temperature getTemperature() {
        return temperature;
    }
}
