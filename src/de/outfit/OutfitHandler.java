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

            if (!params.containsKey("q")) {
                exchange.setResponseCode(422);
                json.writeStartObject();
                json.writeStringField("error", "No location given");
                json.writeEndObject();
                return;
            }
            try {
                Temperature temperature = weather.currentTemperatureForCity(params.get("q").getFirst());
                json.writeStartObject();
                json.writeNumberField("temperature", temperature.getTemperatureInCelsius());
                json.writeStringField("temperature_unit", "Celsius");
                json.writeNumberField("outfit_level", OutfitRecommendation.recommendOutfitFor(temperature).getLevel());
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
}
