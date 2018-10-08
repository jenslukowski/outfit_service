package de.outfit;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import java.util.Deque;
import java.util.Map;

public class OutfitHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "*");
        exchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Methods"), "GET");

        final Map<String, Deque<String>> params = exchange.getQueryParameters();
        if (!params.containsKey("station") || !params.containsKey("at")) {
            return;
        }

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.startBlocking();
    }
}
