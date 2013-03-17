/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taman.rtrc.endpoint.data;

import java.io.StringReader;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author mohamed_taman
 */
public class LoginMessage {
    
    private String username;
    
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static LoginMessage fromJSON(String jsonValue) {

        LoginMessage msg = new LoginMessage();

        try (StringReader reader = new StringReader(jsonValue);
                JsonReader jsonReader = Json.createReader(reader)) {

            JsonObject newObj = jsonReader.readObject();

            for (String key : newObj.keySet()) {

                switch (key) {
                    case "username":
                        msg.setUsername(newObj.getString(key));
                        break;
                    case "password":
                        msg.setPassword(newObj.getString(key)); 
                }
            }
        }
        return msg;
    }

    @Override
    public String toString() {
        return "LoginMessage{" + "username=" + username + ", password=" + password + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.username);
        hash = 47 * hash + Objects.hashCode(this.password);
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
        final LoginMessage other = (LoginMessage) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }

}
