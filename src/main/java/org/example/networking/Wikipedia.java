package org.example.networking;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;


public class Wikipedia {
    public static String getWikipediaInformation(String component) throws IOException {
        final String API_URL = "https://en.wikipedia.org/api/rest_v1/page/summary/";
        String encodedComponent = URLEncoder.encode(component, StandardCharsets.UTF_8).replace("+", "%20");
        // Create a new HttpClient
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        // Create a new HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + encodedComponent))
                .header("Accept", "application/json")
                .GET()
                .build();
        // Send request and get response
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println(response.body());
                throw new IOException("Failed to fetch data from Wikipedia. HTTP code: " +
                        response.statusCode());
            }
            // Parse the JSON response using the third-party library org.json
            return new org.json.JSONObject(response.body()).getString("extract");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}

