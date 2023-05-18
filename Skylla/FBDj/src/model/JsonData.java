package model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.ini4j.Ini;
import org.json.JSONObject;

import mainController.LogController;
import mainController.MainController;
import utility.StringUtilities;

public class JsonData {
    enum RequestType {
        GET("GET"),
        POST("POST");
        private final String typeText;
        private final static RequestType defaultValue = GET;

        RequestType(final String typeText) {
            this.typeText = typeText;
        }

        static RequestType of(String value) {
            try {
                return RequestType.valueOf(value);
            } catch (IllegalArgumentException e) {
                return defaultValue;
            }
        }
        
        static RequestType defaultOr(RequestType value) {
            return value != null ? value : defaultValue;
        }
       
        @Override
        public String toString() {
            return typeText;
        }
    }
    
    enum ResponseFormat {
        JSON("json");
        private final String formatText;
        private final static ResponseFormat defaultValue = JSON;

        ResponseFormat(final String formatText) {
            this.formatText = formatText;
        }

        static ResponseFormat of(String value) {
            try {
                return ResponseFormat.valueOf(value);
            } catch (IllegalArgumentException e) {
                return defaultValue;
            }
        }
        
        static ResponseFormat defaultOr(ResponseFormat value) {
            return value != null ? value : defaultValue;
        }
       
        @Override
        public String toString() {
            return formatText;
        }
    }
    
    enum JsonFieldType {
        STRING("string"),
        INT("int"),
        DOUBLE("double");
        private final String typeText;
        private final static JsonFieldType defaultValue = STRING;

        JsonFieldType(final String typeText) {
            this.typeText = typeText;
        }

        static JsonFieldType of(String value) {
            try {
                return JsonFieldType.valueOf(value);
            } catch (IllegalArgumentException e) {
                return defaultValue;
            }
        }
        
        static JsonFieldType defaultOr(JsonFieldType value) {
            return value != null ? value : defaultValue;
        }
       
        @Override
        public String toString() {
            return typeText;
        }
        
    }
    
    class CustomField {
        private String fieldName;
        private JsonFieldType fieldType;
        private String data;
        CustomField(String fieldName, String fieldType) {
            this.fieldName = fieldName;
            this.fieldType = JsonFieldType.of(fieldType == null?STRING_DEFAULT:fieldType);
            this.data = null;
        }
        
        void setData(String data) {
            this.data = data;
        }

        String getFieldName() {
            return fieldName;
        }

        JsonFieldType getFieldType() {
            return fieldType;
        }

        String getData() {
            return data;
        }
    }
    
    String url;
    RequestType requestType;
    HashMap<String, String> properties;
    HashMap<String, CustomField> customFields;
    String key;
    ResponseFormat responseFormat;
    String statusFieldName;
    JsonFieldType statusFieldType;
    String messageFieldName;
    JsonFieldType messageFieldType;
    JSONObject json;
    
    private static final String REQUEST_TYPE_FIELD = "requesttype";
    private static final String REQUEST_TYPE_DEFAULT = "GET";
    private static final String PROPERTY_FIELD = "property";
    private static final String PROPERTY_KEY = "key";
    private static final String PROPERTY_VALUE = "value";
    private static final String KEY_FIELD = "apiKey";
    private static final String FORMAT_FIELD = "format";
    private static final String FORMAT_DEFAULT = "json";
    private static final String STRING_DEFAULT = "string";
    
    private static final String FIELD_TAG = "field_";
    private static final String TYPE_TAG = "type_";
    
    private static final String IP_TAG = "{IP}";
    private static final String KEY_TAG = "{KEY}";
    private static final int TIMEOUT = 15000;
    
    public JsonData(Ini.Section section, String ipAddress) {
        MainController.writeDebugLogFile(2, "JsonData <Init>+");
        if (section != null && ipAddress != null) {
            this.getSettings(section);
            this.update(ipAddress);
        }
        MainController.writeDebugLogFile(2, "JsonData <Init>-");
    }

    private static <T, S> HashMap<T,S> addKeyValuePair(HashMap<T, S> map, T key, S value) {
        MainController.writeDebugLogFile(2, "JsonData addKeyValuePair+");
        if (map == null) map = new HashMap<>();
        map.put(key, value);
        MainController.writeDebugLogFile(2, "JsonData addKeyValuePair-");
        return map;
    }
    
    boolean getSettings(Ini.Section section) {
        MainController.writeDebugLogFile(2, "JsonData getSettings+");
        this.url = section.get("url");
        this.requestType = JsonData.RequestType.of(section.get(REQUEST_TYPE_FIELD, REQUEST_TYPE_DEFAULT));
        
        for (int propertyIndex = 1;;propertyIndex++) {
            String keyName = PROPERTY_FIELD + propertyIndex + PROPERTY_KEY;
            if (!section.containsKey(keyName)) break;
            String valueName = PROPERTY_FIELD + propertyIndex + PROPERTY_VALUE;
            if (!section.containsKey(valueName)) break;
            this.properties = addKeyValuePair(this.properties, section.get(keyName), section.get(valueName));
        }
        this.key = section.get(KEY_FIELD);
        this.responseFormat = JsonData.ResponseFormat.of(section.get(FORMAT_FIELD, FORMAT_DEFAULT));
        
        section.keySet().forEach(k -> {
            if (k.startsWith(FIELD_TAG)) {
                String fieldName = k.substring(FIELD_TAG.length());
                this.customFields = addKeyValuePair(this.customFields, fieldName, new CustomField(section.get(k), section.get(TYPE_TAG + fieldName)));
            }
        });
        MainController.writeDebugLogFile(2, "JsonData getSettings-");
        return true;
    }
    
    boolean update(String ipAddress) {
        MainController.writeDebugLogFile(2, "JsonData update+");
        String finalUrl = this.url;
        if (finalUrl.contains(IP_TAG)) 
            finalUrl = StringUtilities.stringReplace(finalUrl, IP_TAG, ipAddress);
        if (finalUrl.contains(KEY_TAG)) 
            finalUrl = StringUtilities.stringReplace(finalUrl, KEY_TAG, this.key);
        this.json = readJson(finalUrl);
        if (this.customFields == null) return true;
        this.customFields.forEach((k, c) -> {
            if (json.has(c.fieldName))
                c.setData(json.get(c.fieldName).toString());
            else
                c.setData("N/A");
        });
        MainController.writeDebugLogFile(2, "JsonData update-");
        return true;
    }
    
    private JSONObject readJson(String address) {
        MainController.writeDebugLogFile(2, "JsonData readJson+");
        URL url;
        HttpURLConnection con;
        try {
            url = new URL(address);
            con = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
            MainController.writeDebugLogFile(2, "JsonData readJson 1-");
            return null;
        }
        try (AutoCloseable conc = () -> con.disconnect()){
            con.setRequestMethod(this.requestType.toString());
            if (this.properties != null) this.properties.forEach((k,v) -> con.setRequestProperty(k, v));
            con.setReadTimeout(TIMEOUT);
            con.setConnectTimeout(TIMEOUT);
            con.setDoInput(true);
            
            if (con.getResponseCode() != 200) {
                MainController.writeDebugLogFile(2, "JsonData readJson 2-");
                return null;
            }
            
            InputStream response = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response));

            String line = "";
            StringBuffer message = new StringBuffer();
            while ((line = reader.readLine()) != null)
            {
                message.append(line);
            }
            JSONObject json = new JSONObject(message.toString());
            LogController.writeIPLogFile("JSON Object returned: " + json.toString());
            MainController.writeDebugLogFile(2, "JsonData readJson-");
            return json;
        } catch (Exception ex) {
            System.out.println("Exception occured in JsonDate.readJson:");
            ex.printStackTrace();
            MainController.writeDebugLogFile(2, "JsonData readJson 3-");
            return null;
        }    
    }


}
