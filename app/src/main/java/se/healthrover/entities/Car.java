package se.healthrover.entities;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Car implements Serializable{


    private String localDoaminName;
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

    public String getLocalDomainName() {
        return localDoaminName;
    }

    public void setLocalDomainName(String domainName) {
        this.localDoaminName = domainName;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        URL = "http://" + URL + "/";
        this.URL = URL;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        final Car other = (Car) object;
        if (localDoaminName == null) {
            if (other.localDoaminName != null)
                return false;
        } else if (!localDoaminName.equals(other.localDoaminName))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((localDoaminName == null) ? 0 : localDoaminName.hashCode());
        return result;
    }

    @NotNull
    @Override
    public String toString() {
        return getName();
    }
}
