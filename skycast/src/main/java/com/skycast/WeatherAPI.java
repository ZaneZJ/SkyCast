package com.skycast;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class WeatherAPI {

    public static final String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    // Ideally this should be loaded as a secret, but since it's a free tier API Key,
    // keeping it in plain text for easier testing
    public static final String IP_LOCATOR_API_KEY = "0487008b2b2846b68c32ecc621d1aba5";
    // Ideally this should be loaded as a secret, but since it's a free tier API Key,
    // keeping it in plain text for easier testing
    public static final String OPEN_WEATHER_MAP_API_KEY = "174fddb83a2b27fa3d7d4c23a4487f2a";

    public String getIPData(HttpServletRequest request) {
        // When invoking from localhost, it will always return the following IP -> 0:0:0:0:0:0:0:1
        if(!request.getRemoteAddr().equals(LOCALHOST_IP)) {
            // If not on the localhost then would return the remote address
            // Of the user accessing on the browser
            return request.getRemoteAddr();
        }
        // This currently gets the ip address from the backend user
        try {
            // Create URL object
            URL url = new URL("https://api.ipify.org/?format=json");

            // Create connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // If the response code is 200 (HTTP_OK), read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Get the IP address from the "ip" field
                String ipAddress = jsonResponse.getString("ip");

                // Create the JSON response object
                JSONObject jsonResult = new JSONObject();
                jsonResult.put("ip", ipAddress);

                String ip = jsonResult.getString("ip");
                System.out.println("IP: " + ip);

                return ip;
            } else {
                // If the response code is not 200, handle the error
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getCity(String ipAddress) {
        try {
            String url = "https://api.ipgeolocation.io/ipgeo?apiKey=" + IP_LOCATOR_API_KEY + "&ip=" + ipAddress + "&fields=city";

            // Create URL object
            URL apiUrl = new URL(url);

            // Create connection
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // If the response code is 200 (HTTP_OK), read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                System.out.println(response);

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Get the city information
                String city = jsonResponse.getString("city");

                // Print the city information
                System.out.println("City: " + city);

                return city.toLowerCase();
            } else {
                // If the response code is not 200, handle the error
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getWeatherDataFromAPI(String city) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city.toLowerCase() + "&appid=" + OPEN_WEATHER_MAP_API_KEY + "&units=metric";

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println(response);

                return response.toString();
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCityJsonResponseFromAPI(String city) {
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=" + city.toLowerCase() + "&limit=1&appid=" + OPEN_WEATHER_MAP_API_KEY;

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println(response);

                return response.toString();
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getForecastDataFromAPI(double latitude, double longtitude) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longtitude + "&appid=" + OPEN_WEATHER_MAP_API_KEY + "&units=metric";

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println(response);

                return response.toString();
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
