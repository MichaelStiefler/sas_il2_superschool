package model;

import java.io.Serializable;

public class ReservedName implements Serializable {
    private static final long serialVersionUID = 1L;

    private String            name;
    private String            password;

    public ReservedName(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
