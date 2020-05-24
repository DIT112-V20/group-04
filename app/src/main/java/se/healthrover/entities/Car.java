package se.healthrover.entities;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Car implements Serializable{


    private String ID;
    private String URL;
    private String name;

    public Car(String URL, String name) {
        this.URL = URL;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @NotNull
    @Override
    public String toString() {
        return getName();
    }
}
