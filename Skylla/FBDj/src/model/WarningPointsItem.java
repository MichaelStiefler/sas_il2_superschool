package model;

import java.io.Serializable;

public class WarningPointsItem implements Serializable {

    public enum ItemType {
        INTEGER, BOOLEAN, DOUBLE, STRING
    };

    private static final long serialVersionUID = 1L;

    private String            name;
    private String            label;
    private Object            value;
    private ItemType          itemType;
    private Object            lowLimit         = 0;
    private Object            highLimit        = 0;

    public WarningPointsItem(String name, String label, ItemType itemType, Object value) {
        this.name = name;
        this.label = label;
        this.itemType = itemType;
        this.value = value;
    }

    public WarningPointsItem(String name, String label, ItemType itemType, Object value, Object lowLimit, Object highLimit) {
        this.name = name;
        this.label = label;
        this.itemType = itemType;
        this.value = value;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(Object lowLimit) {
        this.lowLimit = lowLimit;
    }

    public Object getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(Object highLimit) {
        this.highLimit = highLimit;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
