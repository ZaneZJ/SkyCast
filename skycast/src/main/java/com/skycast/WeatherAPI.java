package com.skycast;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
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

    public String getIPData(HttpServletRequest request) {
        if(!request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
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
        String apiKey = "0487008b2b2846b68c32ecc621d1aba5";
        try {
            String url = "https://api.ipgeolocation.io/ipgeo?apiKey=" + apiKey + "&ip=" + ipAddress + "&fields=city";

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

    public void getWeatherDataFromAPI(String city) {
        // no CAPS for city value
        // hardcoded from my user change 531af2461f50b8bd82618834fb7e0015 -> 174fddb83a2b27fa3d7d4c23a4487f2a
        String apiKey = "531af2461f50b8bd82618834fb7e0015";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city.toLowerCase() + "&appid=" + apiKey;

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

                System.out.println(response.toString());
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[] getLatAndLonDataFromAPI(String city) {
        // no CAPS for city value
        // hardcoded from my user change 531af2461f50b8bd82618834fb7e0015 -> 174fddb83a2b27fa3d7d4c23a4487f2a
        String apiKey = "531af2461f50b8bd82618834fb7e0015";
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=" + city.toLowerCase() + "&limit=1&appid=" + apiKey;

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

                // Parse JSON response
                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    JSONObject location = jsonArray.getJSONObject(0);
                    double lat = location.getDouble("lat");
                    double lon = location.getDouble("lon");
                    System.out.println("Latitude: " + lat);
                    System.out.println("Longitude: " + lon);
                    // Read Forecast Data
                    double[] latLonArray = {lat, lon};
                    return latLonArray;
                }
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getForecastDataFromAPI(double latitude, double longtitude) {
        // hardcoded from my user change 531af2461f50b8bd82618834fb7e0015 -> 174fddb83a2b27fa3d7d4c23a4487f2a
        String apiKey = "531af2461f50b8bd82618834fb7e0015";
        String url = "http://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longtitude + "&appid=" + apiKey + "&units=metric";

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

                System.out.println(response.toString());
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
