/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

    private String name;
    private double longitude;
    private double latitude;
    private long timestamp;
    private String notes;

    public RunnerMessage() {
    }

    public RunnerMessage(String name, double longitude, double latitude, long timestamp,String notes) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
        this.notes = notes;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toJSON() {

        String result = null;
        try (StringWriter writer = new StringWriter();
                JsonWriter jsonWriter = Json.createWriter(writer)) {

            JsonObject object = Json.createObjectBuilder().
                    add("name", this.getName()).
                    add("longitude", this.getLongitude()).
                    add("latitude", this.getLatitude()).
                    add("timestamp", this.getTimestamp()).
                     add("notes", this.getNotes())
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
                    case "notes":
                        data.setNotes(newObj.getString(key));
                
                }
            }
        }
        return data;
    }

    @Override
    public String toString() {
        return "RunnerMessage{" + "name=" + name + ", longitude=" + longitude + ", latitude=" + latitude + ", timestamp=" + timestamp + ",  notes=" + notes + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 89 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        hash = 89 * hash + Objects.hashCode(this.notes);
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
        if (!Objects.equals(this.notes, other.notes)) {
            return false;
        }
        return true;
    }
}
