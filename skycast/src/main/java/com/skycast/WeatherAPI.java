package com.skycast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

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

    public void getLatAndLonDataFromAPI(String city) {
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
                    getForecastDataFromAPI(lat, lon);
                }
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
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
