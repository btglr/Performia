package http;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParameterFilter extends Filter {
    private static final String DESCRIPTION = "Parses GET parameters";

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        Map<String, String> requests = parseQuery(exchange);

        exchange.setAttribute("parameters", requests);
        chain.doFilter(exchange);
    }

    private Map<String, String> parseQuery(HttpExchange exchange) throws IOException {
        String query;

        if(exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
            query = br.readLine();
        }

        else {
            URI requestedUri = exchange.getRequestURI();
            query = requestedUri.getRawQuery();
        }

        // TODO faire en sorte d'accepter tous types de caractères (surtout dans le cas de mots de passe !)
        String pattern = "([a-zA-Z_]+)=([a-zA-Z0-9\\-]+)?&?";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(query);

        Map<String, String> requests = new LinkedHashMap<>();

        while(m.find()) {
            requests.put(m.group(1), m.group(2));
        }

        exchange.setAttribute("query", query);

        return requests;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}
