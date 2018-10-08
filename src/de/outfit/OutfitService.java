package de.outfit;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Methods;

public class OutfitService {

    public static void main(String[] args) {
        String host = "";
        int port = 8080;
        // http://api.openweathermap.org/data/2.5/weather?q=Karlsruhe&appid=&units=metric
        final RoutingHandler routes = Handlers.routing()
                .add(Methods.GET, "/outfit", new OutfitHandler());
        final Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(routes)
                .build();
        server.start();
        System.out.printf("Server started and listening on %s:%d.\n", host, port);
    }
}
