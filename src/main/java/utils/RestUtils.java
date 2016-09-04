package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by jayamal on 9/4/16.
 */
public class RestUtils {

    public static String makeRestCall(String urlStr){
        String out = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String line;
            final StringBuffer buffer = new StringBuffer(2048);
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            out = buffer.toString();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static Map<String, String> convertResponseToMap(String response){
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Gson gson = new Gson();
        Map<String, String> responseMap = gson.fromJson(response, type);
        return responseMap;
    }

    public static ImageIcon getImageIcon(String urlStr){
        ImageIcon imageIcon = null;
        if(urlStr != null || !urlStr.isEmpty()) {
            try {
                URL url = new URL(urlStr);
                Image image = ImageIO.read(url);
                imageIcon = new ImageIcon(image.getScaledInstance(75, 111, Image.SCALE_SMOOTH));
            } catch (IOException e) {
                System.out.println("Error fetching image : " + urlStr);
            }
        }
        return imageIcon;
    }
}

