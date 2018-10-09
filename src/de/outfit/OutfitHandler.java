package de.outfit;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Deque;
import java.util.Map;

public class OutfitHandler implements HttpHandler {
    private final Weather weather;

    public OutfitHandler(Weather weather) {
        this.weather = weather;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "*");
        exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Methods"), "GET");
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.startBlocking();

        try (final JsonGenerator json = new JsonFactory().createGenerator(exchange.getOutputStream())) {
            final Map<String, Deque<String>> params = exchange.getQueryParameters();

            try {
                TemperatureAtLocation temperatureAtLocation = getCurrentTemperature(params);
                json.writeStartObject();
                json.writeStringField("city", temperatureAtLocation.getCity());
                json.writeStringField("country", temperatureAtLocation.getCountry());
                json.writeNumberField("temperature", temperatureAtLocation.getTemperature().getTemperatureInCelsius());
                json.writeStringField("temperature_unit", "Celsius");
                json.writeNumberField("outfit_level", OutfitRecommendation.recommendOutfitFor(temperatureAtLocation.getTemperature()).getLevel());
                json.writeEndObject();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                exchange.setResponseCode(422);
                json.writeStartObject();
                json.writeStringField("error", "No location given");
                json.writeEndObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                exchange.setResponseCode(404);
                json.writeStartObject();
                json.writeStringField("error", "Could not find location");
                json.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
                exchange.setResponseCode(503);
                json.writeStartObject();
                json.writeStringField("error", "Could not make recommendation");
                json.writeEndObject();
            }
        }
    }

    private TemperatureAtLocation getCurrentTemperature(Map<String, Deque<String>> params) throws IOException, IllegalArgumentException {
        if (params.containsKey("q")) {
            return weather.currentTemperatureForCity(params.get("q").getFirst());
        }
        if (params.containsKey("id")) {
            return weather.currentTemperatureForCityId(Integer.parseInt(params.get("id").getFirst()));
        }
        if (params.containsKey("lat") && params.containsKey("lon")) {
            return weather.currentTemperatureForLocation(
                    Double.parseDouble(params.get("lat").getFirst()),
                    Double.parseDouble(params.get("lon").getFirst()));
        }
        if (params.containsKey("zip")) {
            return weather.currentTemperatureForZip(params.get("zip").getFirst());
        }
        throw new IllegalArgumentException("No location given");
    }
}
