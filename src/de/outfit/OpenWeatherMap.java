package de.outfit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenWeatherMap implements Weather {

    private final String apiKey;

    public OpenWeatherMap(String apiKey) {
        this.apiKey = apiKey;
    }

    // http://api.openweathermap.org/data/2.5/weather?q=Karlsruhe&appid=&units=metric
    @Override
    public Temperature currentTemperatureFor(String city) {
        try {
            String json = json(city);
            ObjectMapper mapper = new ObjectMapper();
            String main = subtree(mapper, json, "main");
            return new Temperature(getDouble(mapper, main, "temp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private URL urlFor(String city) throws UnsupportedEncodingException, MalformedURLException {
        return new URL("http://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(city, "UTF-8") + "&appid=" + URLEncoder.encode(apiKey, "UTF-8") + "&units=metric");
    }

    private String json(String city)  throws IOException {
        try (InputStream input = urlFor(city).openStream()) {
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