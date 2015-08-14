package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.GunProperties;
import com.maddox.rts.Property;

public class TorpedoGun extends BombGun
{

    public TorpedoGun()
    {
    }
    
    // TODO: Storebror: Torpedo Failure Rate Replication
    // ------------------------------------
//    public void setTorpLimitSeed(int torpLimitSeed) {
//        System.out.println("setTorpParamsEx(" + torpLimitSeed + "), isTorpedo=" + (this.bomb instanceof Torpedo));
//        if (!(this.bomb instanceof Torpedo)) return;
//        Torpedo torpedo = (Torpedo)this.bomb;
//        torpedo.setLimitSeed(torpLimitSeed);
//    }
    public void setLimits(float speedLimit, float angleLimitLow, float angleLimitHigh) {
        this.speedLimit = speedLimit;
        this.angleLimitLow = angleLimitLow;
        this.angleLimitHigh = angleLimitHigh;
    }
    
    protected void interpolateStep()
    {
        if (this.bomb != null) {
            if (this.bomb instanceof Torpedo) {
                ((Torpedo)this.bomb).setLimits(this.speedLimit, this.angleLimitLow, this.angleLimitHigh);
            }
        }
        super.interpolateStep();
    }
    // ------------------------------------

    public float TravelTime(Point3d point3d, Point3d point3d1)
    {
        float f = (float)point3d.distance(point3d1);
        Class class1 = getClass();
        Class class2 = (Class)Property.value(class1, "bulletClass", null);
        float f1 = Property.floatValue(class2, "velocity", 1.0F);
        float f2 = Property.floatValue(class2, "traveltime", 1.0F);
        if(f > f1 * f2)
            return -1F;
        else
            return f / f1;
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = new GunProperties();
        gunproperties.weaponType = 16;
        return gunproperties;
    }
    
    private float speedLimit;
    private float angleLimitLow;
    private float angleLimitHigh;
}
