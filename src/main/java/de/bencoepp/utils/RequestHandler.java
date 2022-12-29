package de.bencoepp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHandler {
    public static String get(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            StringBuilder responseContent = new StringBuilder();
            BufferedReader reader;
            String line;
            if (status >= 300) {
                reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            else {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
            return responseContent.toString();
        }catch (Exception e){
            return e.toString();
        }
    }

}
