package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONObject;

@FunctionalInterface
public interface JsonResponse {
    @SuppressWarnings("unchecked")
    JSONObject response(String ip, String key, Map.Entry<String,String> ... properties);
    
    @SuppressWarnings("unchecked")
    default JSONObject getResponse(String address, Map.Entry<String,String> ... properties) {
        if (properties == null || properties.length == 0) {
            try (InputStream is = new URL(address).openStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String line = "";
                StringBuilder message = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    message.append(line);
                }
                return new JSONObject(message.toString());
            } catch (Exception ex) {
                System.out.println("Exception occured in JsonResponse.getResponse:");
                ex.printStackTrace();
                return null;
            }    
        } else {

            URL abuseIpDbUrl = null;
            try {
                abuseIpDbUrl = new URL(address);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            final HttpURLConnection con;
            try {
                con = (HttpURLConnection) abuseIpDbUrl.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            try (AutoCloseable conc = () -> con.disconnect()) {
                con.setRequestMethod("GET");
                Stream.of(properties).forEach((e) -> con.setRequestProperty(e.getKey(), e.getValue()));
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setDoInput(true);

                if (con.getResponseCode() != 200)
                    return null;

                InputStream response = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, Charset.forName("UTF-8")));

                String line = "";
                StringBuilder message = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    message.append(line);
                }
                return new JSONObject(message.toString());
            } catch (Exception ex) {
                System.out.println("Exception occured in JsonResponse.getResponse:");
                ex.printStackTrace();
                return null;
            }
        }
    }
}
