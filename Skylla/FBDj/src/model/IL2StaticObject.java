package model;

import java.io.Serializable;

/*
 * Stores all targets available in IL2
 */

public class IL2StaticObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ObjectType {
        AAA, ARTILLERY, TANK, SHIP, SPLANE, PLANE, PILOT, CAR, WAGON, BRIDGE, AIPLANE, MISC, RADIO
    }

    private String     objectName;
    // Type of object (AAA, Artillery, Car, etc.)
    private ObjectType objectType  = ObjectType.MISC;
    // Name to be stored in database and used for stats
    private String     displayName;

    // Stats Database ID
    private int        il2ObjectID = 0;
    private int        pointValue  = 0;

//	private int             baseValue = 0;

    public IL2StaticObject(String name, ObjectType objectType, String displayName) {
        this.objectName = name;
        this.objectType = objectType;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public String getObjectName() {
        return objectName;
    }

    public int getIl2ObjectID() {
        return il2ObjectID;
    }

    public void setIl2ObjectID(int il2ObjectID) {
        this.il2ObjectID = il2ObjectID;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

//	public int getBaseValue() {
//		return baseValue;
//	}
//
//	public void setBaseValue(int baseValue) {
//		this.baseValue = baseValue;
//	}

}
