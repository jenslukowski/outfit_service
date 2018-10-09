package de.outfit;

import java.io.IOException;

public interface Weather {
    Temperature currentTemperatureForCity(String city) throws IOException;

    Temperature currentTemperatureForCityId(int id) throws IOException;

    Temperature currentTemperatureForLocation(double latitude, double longitude) throws IOException;

    Temperature currentTemperatureForZip(String zip) throws IOException;
}
