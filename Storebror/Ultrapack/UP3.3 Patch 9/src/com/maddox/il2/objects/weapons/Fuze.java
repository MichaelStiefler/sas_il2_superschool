package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.AircraftLH;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Fuze {

    public Fuze() {
        this.startingPoint = new Point3d();
        this.isArmed = true;
        this.armingTime = -1;
        this.pathV = new Vector3d();
        this.overridingDelay = -1F;
        this.bomb = null;
        this.fuzeMode = 0;
    }

    public static float cvt(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
        paramFloat1 = Math.min(Math.max(paramFloat1, paramFloat2), paramFloat3);
        return paramFloat4 + (((paramFloat5 - paramFloat4) * (paramFloat1 - paramFloat2)) / (paramFloat3 - paramFloat2));
    }

    public static void setStartTime() {
        Fuze.missionStartTime = World.getTimeofDay();
    }

    public int getFuzeType() {
        return Property.intValue(this.getClass(), "type", -1);
    }

    public void rareAction(ActorPos actorpos, Vector3d vector3d) {
    }

    public void setOwnerBomb(Bomb paramBomb) {
        this.bomb = paramBomb;
    }

    public Bomb getOwnerBomb() {
        return this.bomb;
    }

    public float getDetonationDelay() {
        return this.overridingDelay;
    }

    public void setDetonationDelay(float paramFloat) {
        this.overridingDelay = paramFloat;
        if (this.bomb != null) {
            this.bomb.delayExplosion = this.overridingDelay;
        }
    }

    public void setArmingTime(int paramInt) {
        this.armingTime = paramInt;
    }

    public int getArmingTime() {
        return this.armingTime;
    }

    public void setStartingTime(long paramLong) {
        this.startingTime = paramLong;
    }

    public void setStartingPoint(Point3d paramPoint3d) {
        this.startingPoint.set(paramPoint3d);
    }

    public boolean isArmed() {
        return this.isArmed;
    }

    public boolean isFuzeArmed(ActorPos paramActorPos, Vector3d paramVector3d, Actor paramActor) {
        float f = Property.floatValue(this.getClass(), "airTravelToArm", -1F);
        if (f == 0.0F) {
            this.isArmed = true;
        } else if (f == -1F) {
            if (this.armingTime == -1) {
                this.armingTime = Property.intValue(this.getClass(), "armingTime", 2000);
            }
            long l = Time.current() - this.startingTime;
            if (l < this.armingTime) {
                this.isArmed = false;
            } else {
                this.isArmed = true;
            }
        } else {
            this.pathV.sub(this.startingPoint, paramActorPos.getCurrentPoint());
            double d = this.pathV.length();
            if (d < f) {
                this.isArmed = false;
            } else {
                this.isArmed = true;
            }
        }
        return this.isArmed;
    }

    public static Fuze selectFuze(Class fuzeClass, int fuzeType, float fuzeDelay, boolean doSetInfo) {
        Fuze fuze = null;
        int missionDate = Mission.getMissionDate(false);
        Object[] ao = (Object[]) Property.value(fuzeClass, "fuze", null);
        Object o;
        if (ao != null) {
            o = null;
            int foundFuzeClassIndex = 0;
            float minDelay1;
            for (int fuzeClassIndex = 0; fuzeClassIndex < ao.length; fuzeClassIndex++) {
                Class curFuzeClass = (Class) ao[fuzeClassIndex];
                int curFuzeType = Property.intValue(curFuzeClass, "type", 0);
                if (curFuzeType != fuzeType) {
                    if ((fuzeType == 2) && (curFuzeType == 1)) {
                        foundFuzeClassIndex = fuzeClassIndex;
                    }
                    if ((fuzeType == 1) && (curFuzeType == 2)) {
                        foundFuzeClassIndex = fuzeClassIndex;
                    }
                } else {
                    minDelay1 = Property.floatValue(curFuzeClass, "minDelay", -1.0F);
                    float maxDelay1 = Property.floatValue(curFuzeClass, "maxDelay", -1.0F);
                    float[] aFixedDelays1 = (float[]) Property.value(curFuzeClass, "fixedDelay", null);
                    if ((minDelay1 != -1.0F) && (maxDelay1 != -1.0F) ?

                            (fuzeDelay <= maxDelay1) || (fuzeDelay >= minDelay1) :

                            (aFixedDelays1 == null) || (

                            (fuzeDelay <= aFixedDelays1[(aFixedDelays1.length - 1)]) && (fuzeDelay >= aFixedDelays1[0]))) {
                        int dateStart = Property.intValue(curFuzeClass, "dateStart", 0);
                        int dateEnd = Property.intValue(curFuzeClass, "dateEnd", 19999999);
                        if ((missionDate >= dateStart) && (missionDate <= dateEnd)) {
                            o = curFuzeClass;
                        }
                    }
                }
            }
            if (o == null) {
                o = ao[foundFuzeClassIndex];
                float minDelay2 = Property.floatValue((Class) o, "minDelay", -1.0F);
                float maxDelay2 = Property.floatValue((Class) o, "maxDelay", -1.0F);
                float[] aFixedDelays2 = (float[]) Property.value((Class) o, "fixedDelay", null);
                if ((minDelay2 != -1.0F) && (maxDelay2 != -1.0F)) {
                    if (fuzeDelay > maxDelay2) {
                        fuzeDelay = maxDelay2;
                    } else if (fuzeDelay < minDelay2) {
                        fuzeDelay = minDelay2;
                    }
                } else if (aFixedDelays2 != null) {
                    if (fuzeDelay > aFixedDelays2[(aFixedDelays2.length - 1)]) {
                        fuzeDelay = aFixedDelays2[(aFixedDelays2.length - 1)];
                    } else if (fuzeDelay < aFixedDelays2[0]) {
                        fuzeDelay = aFixedDelays2[0];
                    } else {
                        minDelay2 = 10000.0F;
                        int foundFuzeDelayIndex = 0;
                        for (int fuzeDelayIndex = 0; fuzeDelayIndex < aFixedDelays2.length; fuzeDelayIndex++) {
                            float curDelay = Math.abs(aFixedDelays2[fuzeDelayIndex] - fuzeDelay);
                            if (curDelay < minDelay2) {
                                minDelay2 = curDelay;
                                foundFuzeDelayIndex = fuzeDelayIndex;
                            }
                        }
                        fuzeDelay = aFixedDelays2[foundFuzeDelayIndex];
                    }
                } else {
                    fuzeDelay = 0.0F;
                }
            }
            try {
                fuze = (Fuze) ((Class) o).newInstance();
                fuze.setDetonationDelay(fuzeDelay);
                String armingName = fuze.getClass().toString().substring(37);
                String bombName = fuzeClass.toString().substring(41);
                if (doSetInfo) {
                    AircraftLH.setInfo(fuze, bombName, armingName, fuzeDelay);
                }
                return fuze;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            fuze = Fuze.getGenericFuze(fuzeClass);
            o = fuze.getClass().toString().substring(37);
            String bombName = fuzeClass.toString().substring(41);
            if (doSetInfo) {
                AircraftLH.setInfo(fuze, bombName, (String) o, 0.0F);
            }
            return fuze;
        } catch (Exception e) {
        }
        return null;
    }

    private static Fuze getGenericFuze(Class class1) {
        float f = Property.floatValue(class1, "massa", 0.0F);
        try {
            if (f < 75.0F) {
                return (Fuze) Fuze_generic_1sec.class.newInstance();
            }
            return (Fuze) Fuze_generic_2sec.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final int         FUZE_TYPE_INSTANT            = 0;
    public static final int         FUZE_TYPE_DELAY              = 1;
    public static final int         FUZE_TYPE_LOWLEVEL           = 2;
    public static final int         FUZE_TYPE_LONGDELAY          = 3;
    public static final int         FUZE_TYPE_ELECTRIC           = 4;
    public static final int         FUZE_TYPE_ELECTRIC_SEA       = 5;
    public static final int         FUZE_TYPE_ELECTRIC_LONGDELAY = 6;
    public static final int         FUZE_TYPE_ELECTRIC_AIRBUSRT  = 7;
    public static final int         FUZE_TYPE_BAROMETRIC         = 8;
    public static final int         FUZE_TYPE_HYDROSTATIC        = 9;
    public static final int         FUZE_TYPE_PROXIMITY          = 10;
    protected long                  startingTime;
    private Point3d                 startingPoint;
    protected boolean               isArmed;
    protected int                   armingTime;
    private Vector3d                pathV;
    private float                   overridingDelay;
    private Bomb                    bomb;
    protected static float          missionStartTime             = -1F;
    protected static final Vector3d land                         = new Vector3d(0.0D, 0.0D, -1D);
    public int                      fuzeMode;

}
