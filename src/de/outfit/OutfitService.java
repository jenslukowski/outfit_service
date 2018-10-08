package de.outfit;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Methods;

/**
 * OutfitService
 * Provides a REST endpoint for an outfit recommendation based on the current temperature at a given location
 * The location can be given in the following ways:
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
 * Content:
 *
 * Error Response:
 * Code 422
 * Content:
 *
 * Example:
 * /outfit?q=Karlsruhe,DE
 */
public class OutfitService {

    public static void main(String[] args) {
        String host = "";
        int port = 8080;
        String apiKey = valueFor(args, "--key=", "");
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
}
