package com.dashboard.api;

import com.dashboard.model.Option;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OptionFetcher {

    public static List<Option> fetchOptions(String ticker) {
        List<Option> optionsList = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8000/options/" + ticker); // Call your Python server
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Java");
            conn.setRequestMethod("GET");

            InputStream in = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(in);

            JsonNode calls = root.path("calls");
            JsonNode puts = root.path("puts");

            for (JsonNode call : calls) {
                Option opt = new Option(
                        ticker,
                        call.path("strike").asDouble(),
                        call.path("expiration").asText("N/A"),
                        "CALL",
                        call.path("bid").asDouble(0.0),
                        call.path("ask").asDouble(0.0),
                        call.path("impliedVolatility").asDouble(0.0)
                );
                optionsList.add(opt);
            }

            for (JsonNode put : puts) {
                Option opt = new Option(
                        ticker,
                        put.path("strike").asDouble(),
                        put.path("expiration").asText("N/A"),
                        "PUT",
                        put.path("bid").asDouble(0.0),
                        put.path("ask").asDouble(0.0),
                        put.path("impliedVolatility").asDouble(0.0)
                );
                optionsList.add(opt);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to call Python API: " + e.getMessage());
        }

        return optionsList;
    }
}
