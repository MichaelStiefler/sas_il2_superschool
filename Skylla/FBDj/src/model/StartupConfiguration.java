package model;

import java.io.Serializable;

public class StartupConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    private String            configDirectory  = "";

    public StartupConfiguration(String configDirectory) {
        this.configDirectory = configDirectory;
    }

    public String getConfigDirectory() {
        return configDirectory;
    }

    public void setConfigDirectory(String configDirectory) {
        this.configDirectory = configDirectory;
    }
}
