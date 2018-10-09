package de.outfit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

public class OpenWeatherMap implements Weather {
    private static final String openWeatherMapURL = "http://api.openweathermap.org/data/2.5/weather";

    private final String apiKey;

    public OpenWeatherMap(String apiKey) {
        this.apiKey = apiKey;
    }

    // http://api.openweathermap.org/data/2.5/weather?q=Karlsruhe&appid=&units=metric
    @Override
    public Temperature currentTemperatureForCity(String city) throws IOException {
        return currentTemperatureFor(urlForCity(city));
    }

    @Override
    public Temperature currentTemperatureForCityId(int id) throws IOException {
        return currentTemperatureFor(urlForCityId(id));
    }

    @Override
    public Temperature currentTemperatureForLocation(double latitude, double longitude) throws IOException {
        return currentTemperatureFor(urlForLocation(latitude, longitude));
    }

    @Override
    public Temperature currentTemperatureForZip(String zip) throws IOException {
        return currentTemperatureFor(urlForZip(zip));
    }

    private URL urlForCity(String city) throws UnsupportedEncodingException, MalformedURLException {
        return new URL(openWeatherMapURL + "?q=" + URLEncoder.encode(city, "UTF-8") + "&appid=" + URLEncoder.encode(apiKey, "UTF-8") + "&units=metric");
    }

    private URL urlForCityId(int cityId) throws UnsupportedEncodingException, MalformedURLException {
        return new URL(openWeatherMapURL + "?id=" + cityId + "&appid=" + URLEncoder.encode(apiKey, "UTF-8") + "&units=metric");
    }

    private URL urlForLocation(double latitude, double longitude) throws UnsupportedEncodingException, MalformedURLException {
        return new URL(openWeatherMapURL + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + URLEncoder.encode(apiKey, "UTF-8") + "&units=metric");
    }

    private URL urlForZip(String zip) throws UnsupportedEncodingException, MalformedURLException {
        return new URL(openWeatherMapURL + "?zip=" + URLEncoder.encode(zip, "UTF-8") + "&appid=" + URLEncoder.encode(apiKey, "UTF-8") + "&units=metric");
    }

    private Temperature currentTemperatureFor(URL url) throws IOException {
        String json = callWeatherServiceFor(url);
        ObjectMapper mapper = new ObjectMapper();
        String main = subtree(mapper, json, "main");
        return new Temperature(getDouble(mapper, main, "temp"));
    }

    private String callWeatherServiceFor(URL url) throws IOException {
        try (InputStream input = url.openStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    private String subtree(ObjectMapper mapper, String json, String key) throws IOException {
        final ObjectNode root = mapper.readValue(json, ObjectNode.class);
        return root.get(key).toString();
    }

    private double getDouble(ObjectMapper mapper, String json, String key) throws IOException {
        final ObjectNode root = mapper.readValue(json, ObjectNode.class);
        return root.get(key).doubleValue();
    }
}
