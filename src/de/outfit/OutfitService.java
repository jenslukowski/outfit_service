package de.outfit;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Methods;

/**
 * OutfitService
 * Provides a REST endpoint for an outfit recommendation based on the current temperature at a given location
 * The location can be given in the following ways:
 *
 * /outfit?q=:city
 * /outfit?q=:city,:country
 * /outfit?id=:city_id
 * /outfit?lat=:latitude&:lon=:longitude
 * /outfit?zip=:zip
 * /outfit?zip=:zip,:country
 *
 * where
 * city=[string] - the name of a city
 * country=[string] - the country code
 * id=[integer] - the id of a city
 * lat,lon=[integer] - the coordinates of the location
 * zip=[integer] - the zip code of the location, if the country code is omitted USA is assumed
 *
 * Success Response:
 * Code: 200
 * Content: {"temperature": 10.53, "temperature_unit": "Celsius", "outfit_level": 4}
 *
 * Error Response:
 * Code 404
 * Content: {"error": "Could not find location"}
 *
 * Code 422
 * Content: {"error": "No location given"}
 *
 * Code 503
 * Content: {"error": "Could not make recommendation"}
 *
 * Example:
 *
 * Request: /outfit?q=Karlsruhe,DE
 * Response: {"temperature": 10.53, "temperature_unit": "Celsius", "outfit_level": 4}
 */
public class OutfitService {

    public static void main(String[] args) {
        String host = valueFor(args, "--host=", "");
        int port = intValueFor(args, "--port=", 8080);
        String apiKey = valueFor(args, "--key=", "");
        if ("".equals(apiKey)) {
            System.out.println("Please specify an api key with the --key= option");
            System.out.println("Example:");
            System.out.println("java -jar outfit_service.jar --key=12345");
            return;
        }
        final RoutingHandler routes = Handlers.routing()
                .add(Methods.GET, "/outfit", new OutfitHandler(new OpenWeatherMap(apiKey)));
        final Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(routes)
                .build();
        server.start();
        System.out.printf("Outfit Service started and listening on %s:%d.\n", host, port);
    }

    private static String valueFor(String[] args, String parameter, String defaultValue) {
        for (String arg : args) {
            if (arg.startsWith(parameter)) {
                return arg.substring(parameter.length());
            }
        }
        return defaultValue;
    }

    private static int intValueFor(String[] args, String parameter, int defaultValue) {
        try {
            return Integer.parseInt(valueFor(args, parameter, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
