package model;

import java.io.Serializable;

public class PlaneLimit implements Serializable {
    private static final long serialVersionUID = 1L;

    private String            planeName;
    private int               planesInUseLimit = 0;
    private int               planeTotalLimit  = 0;
    private int               planesInUse      = 0;
    private int               planesLost       = 0;
    private boolean           basedOnPercent   = false;

    public PlaneLimit(String planeName, int inUseLimit, int totalLimit) {
        this.planeName = planeName;
        this.planesInUseLimit = inUseLimit;
        this.planeTotalLimit = totalLimit;
    }

    public boolean isBasedOnPercent() {
        return basedOnPercent;
    }

    public void setBasedOnPercent(boolean basedOnPercent) {
        this.basedOnPercent = basedOnPercent;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public int getPlanesInUseLimit() {
        return planesInUseLimit;
    }

    public void setPlanesInUseLimit(int planesInUseLimit) {
        this.planesInUseLimit = planesInUseLimit;
    }

    public int getPlaneTotalLimit() {
        return planeTotalLimit;
    }

    public void setPlaneTotalLimit(int planeTotalLimit) {
        this.planeTotalLimit = planeTotalLimit;
    }

    public int getPlanesInUse() {
        return planesInUse;
    }

    public void setPlanesInUse(int planesInUse) {
        this.planesInUse = planesInUse;
    }

    public int getPlanesLost() {
        return planesLost;
    }

    public void setPlanesLost(int planesLost) {
        this.planesLost = planesLost;
    }
}
