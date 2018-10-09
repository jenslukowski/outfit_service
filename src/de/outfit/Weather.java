package de.outfit;

import java.io.IOException;

public interface Weather {
    TemperatureAtLocation currentTemperatureForCity(String city) throws IOException;

    TemperatureAtLocation currentTemperatureForCityId(int id) throws IOException;

    TemperatureAtLocation currentTemperatureForLocation(double latitude, double longitude) throws IOException;

    TemperatureAtLocation currentTemperatureForZip(String zip) throws IOException;
}
