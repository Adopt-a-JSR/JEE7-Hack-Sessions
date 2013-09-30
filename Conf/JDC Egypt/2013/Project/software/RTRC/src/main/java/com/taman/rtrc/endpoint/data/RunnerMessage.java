package com.taman.rtrc.endpoint.data;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

/**
 *
 * @author mohamed_taman
 */
public class RunnerMessage {

    private String action = "UPDATE";
    private String userId;
    private String name;
    private double latitude;
    private double longitude;
    private long timestamp;
    
     /*
       {action: "UPDATE", userId: "New Marker", 
        name: "Marker added", latitude:37.785054, 
	longitude:-122.411161,timestamp:0,
	accuracy:0};
        * 
        * "{\"action\":\"UPDATE\",\"message\":\"All available runneres in the system.\"}";
      
      */

    public RunnerMessage() {
    }

    public RunnerMessage(String action, String userId, String name, double longitude, double latitude, long timestamp) {
        this.action = action;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
        this.userId = userId;
    }

     public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String toJSON() {

        String result = null;
        try (StringWriter writer = new StringWriter();
                JsonWriter jsonWriter = Json.createWriter(writer)) {

            JsonObject object = Json.createObjectBuilder().
                    add("action", this.getAction()).
                    add("userId", this.getUserId()).
                    add("name", this.getName()).
                    add("latitude", this.getLatitude()).
                    add("longitude", this.getLongitude()).
                    add("timestamp", this.getTimestamp())
                    .build();

            jsonWriter.writeObject(object);

            result = writer.toString();

        } catch (IOException e) {
        }

        return result;
    }

    public static RunnerMessage fromJSON(String jsonValue) {

        RunnerMessage data = new RunnerMessage();

        try (StringReader reader = new StringReader(jsonValue);
                JsonReader jsonReader = Json.createReader(reader)) {

            JsonObject newObj = jsonReader.readObject();

            for (String key : newObj.keySet()) {

                switch (key) {
                    case "name":
                        data.setName(newObj.getString(key));
                        break;
                    case "latitude":
                        data.setLatitude(newObj.getJsonNumber(key).doubleValue());
                        break;
                    case "longitude":
                        data.setLongitude(newObj.getJsonNumber(key).doubleValue());
                        break;
                    case "timestamp":
                        data.setTimestamp(newObj.getJsonNumber(key).longValue());
                        break;
                        case "action":
                        data.setAction(newObj.getString(key));
                        break;
                    case "userId":
                        data.setUserId(newObj.getString(key));
                
                }
            }
        }
        return data;
    }

    @Override
    public String toString() {
        return "RunnerMessage{" + "name=" + name + ", longitude=" + longitude + ", latitude=" + latitude + ", timestamp=" + timestamp + ",  userId=" + userId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 89 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        hash = 89 * hash + Objects.hashCode(this.userId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RunnerMessage other = (RunnerMessage) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
            return false;
        }
        if (this.timestamp != other.timestamp) {
            return false;
        }
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }
}
