package de.bencoepp.utils.firebase.service;

import de.bencoepp.utils.firebase.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserService {
    private String apiKey;
    public UserService(String apiKey) {
        this.apiKey = apiKey;
    }

    public User signUpUser(String email, String password) throws IOException {
        URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + apiKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"password\": \"" + password + "\",\n" +
                "    \"returnSecureToken\": true\n" +
                "}";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = con.getResponseCode();
        StringBuilder responseContent = new StringBuilder();
        BufferedReader reader;
        String line;
        if (responseCode >= 300) {
            reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
        }
        else {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
        }
        System.out.println(responseContent.toString());
        User user = new User();
        user.fromJson("{\"user\":" + responseContent + "}");
        return user;
    }

    public User loginUser(String email, String password) throws IOException {
        URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"password\": \"" + password + "\",\n" +
                "    \"returnSecureToken\": true\n" +
                "}";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = con.getResponseCode();
        StringBuilder responseContent = new StringBuilder();
        BufferedReader reader;
        String line;
        if (responseCode >= 300) {
            reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
        }
        else {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
        }
        System.out.println(responseContent.toString());
        User user = new User();
        user.fromJson("{\"user\":" + responseContent + "}");
        return user;
    }
}
