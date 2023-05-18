package model;

import java.io.Serializable;

public class ConfigurationItem implements Serializable {
    public enum ChatReciepients {
        ALL, ARMY, PILOT, ADMIN, NONE
    };

    public enum ConfigItemType {
        INTEGER, BOOLEAN, DOUBLE, STRING, CHATRECIEPIENTS, MISSIONCYCLE
    };

    private static final long serialVersionUID = 1L;

    private String            name;
    private String            label;
    private ConfigItemType    itemType;
    private Object            value;
    private Object            lowLimit         = 0;
    private Object            highLimit        = 0;

    public ConfigurationItem(String name, String label, ConfigItemType itemType, Object value) {
        this.name = name;
        this.label = label;
        this.itemType = itemType;
        this.value = value;
    }

    public ConfigurationItem(String name, String label, ConfigItemType itemType, Object value, Object lowLimit, Object highLimit) {
        this.name = name;
        this.label = label;
        this.itemType = itemType;
        this.value = value;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public ConfigItemType getItemType() {
        return itemType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(long lowLimit) {
        this.lowLimit = lowLimit;
    }

    public Object getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(long highLimit) {
        this.highLimit = highLimit;
    }

}
