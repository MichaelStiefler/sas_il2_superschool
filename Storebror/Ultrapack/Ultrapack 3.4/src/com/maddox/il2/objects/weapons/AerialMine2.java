package com.maddox.il2.objects.weapons;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.MsgCollision;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.LDRres;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class AerialMine2 extends Bomb {

    public void msgCollision(Actor actor, String s, String s1) {
        Class class1 = this.getClass();
        this.armingTime = (long) Property.floatValue(class1, "armingTime", 1.0F) * 1000L;
        this.Other = actor;
        this.OtherChunk = s1;
        if (actor instanceof ActorLand) {
            if (this.flow) {
                this.doExplosion(actor, s1);
                return;
            }
            this.surface();
            if (World.land().isWater(AerialMine2.P.x, AerialMine2.P.y)) {
                return;
            }
        }
        if (this.flow) {
            if ((this.getOwner() instanceof Aircraft) && !(this instanceof BombParaInfluenceMine2) && !(this instanceof BombInfluenceMine2)) {
                Aircraft aircraft1 = (Aircraft) this.getOwner();
                if (aircraft1.FM.isPlayers()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Hit");
                }
            }
            if ((this.getOwner() instanceof Aircraft) && ((this instanceof BombParaInfluenceMine2) || (this instanceof BombInfluenceMine2))) {
                Aircraft aircraft1 = (Aircraft) this.getOwner();
                if (aircraft1.FM.isPlayers()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Activated");
                }
            }
            this.doExplosion(actor, s1);
        }
        this.doExplosion(actor, s1);
    }

    private void surface() {
        Class class1 = this.getClass();
        boolean flag = false;
        this.travelTime = (long) Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
        this.dropSpeed = Property.floatValue(class1, "dropSpeed", 1.0F);
        this.dropMaxAltitude = Property.floatValue(class1, "dropMaxAltitude", 1.0F);
        this.dropMinAltitude = Property.floatValue(class1, "dropMinAltitude", 1.0F);
        if ((this instanceof BombParaInfluenceMine2) || (this instanceof BombParaAerialMine2)) {
            float f2 = (float) AerialMine2.spd.length();
            float f5 = 5F / f2;
            AerialMine2.spd.scale(f5);
            this.setSpeed(AerialMine2.spd);
        }
        super.pos.getAbs(AerialMine2.P, AerialMine2.Or);
        this.flow = true;
        this.getSpeed(AerialMine2.spd);
        if (World.land().isWater(AerialMine2.P.x, AerialMine2.P.y)) {
            if (AerialMine2.spd.z < -0.12D) {
                Explosions.Explode10Kg_Water(AerialMine2.P, 4F, 1.0F);
            }
//                Explosions.torpedoEnter_Water(P, 4F, 1.0F);
            double d = AerialMine2.spd.length();
            double dd = d * 3.6D;
            int k = (int) dd;
            String MineSpeed = String.valueOf(k);
            double l = AerialMine2.spd.z;
            double ll = l * 3.6D;
            int m = (int) ll;
            String Vspeed = String.valueOf(m);
            double n = (dd * dd) - (ll * ll);
            double nn = Math.sqrt(n);
            int p = (int) nn;
            String Hspeed = String.valueOf(p);
            double h = (l * l) / 19.62D;
            int hh = (int) h;
            String DropAlt = String.valueOf(hh);
            Aircraft aircraft1 = null;
            boolean flag1 = false;
            if ((this.getOwner() instanceof Aircraft) && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && AerialMine2.isDiffFragileTorps()) {
                flag1 = true;
                aircraft1 = (Aircraft) this.getOwner();
            }
            if (flag1 && (p > this.dropSpeed) && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && AerialMine2.isDiffFragileTorps()) {
                if ((aircraft1 != null) && aircraft1.FM.isPlayers()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Damaged at Entry in Water!");
                }
                HUD.log(AircraftHotKeys.hudLogPowerId, "Dropping Speed Too High!MineSpeed = " + MineSpeed + "Drop Speed = " + Hspeed + " Vertical Speed =" + Vspeed + " Drop Height = " + DropAlt);
                flag = true;
                this.destroy();
            }
            if (flag1 && (hh > this.dropMaxAltitude) && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && AerialMine2.isDiffFragileTorps()) {
                if ((aircraft1 != null) && aircraft1.FM.isPlayers()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Destroyed at Entry in Water!");
                }
                HUD.log(AircraftHotKeys.hudLogPowerId, "Dropping Altitude Too High!MineSpeed = " + MineSpeed + "Drop Speed = " + Hspeed + " Vertical Speed =" + Vspeed + " Drop Height = " + DropAlt);
                flag = true;
                this.destroy();
            }
            if (flag1 && (hh < this.dropMinAltitude) && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && AerialMine2.isDiffFragileTorps()) {
                if ((aircraft1 != null) && aircraft1.FM.isPlayers()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Did Not Arm at Entry in Water!");
                }
                HUD.log(AircraftHotKeys.hudLogPowerId, "Dropping Altitude Too Low!MineSpeed = " + MineSpeed + "Drop Speed = " + Hspeed + " Vertical Speed =" + Vspeed + " Drop Height = " + DropAlt);
                flag = true;
                this.destroy();
            }
            if (flag1 && (p < this.dropSpeed) && (hh < this.dropMaxAltitude) && (hh > this.dropMinAltitude) && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && AerialMine2.isDiffFragileTorps()) {
                if ((aircraft1 != null) && aircraft1.FM.isPlayers()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Good Entry! ");
                }
                flag = false;
            }
        } else {
            float f1 = World.Rnd().nextFloat(1.0F, 3F);
            if (f1 <= 2.0F) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Exploded!");
                flag = true;
                this.hasHitLand = true;
                this.destroy();
            } else {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Dud!");
                flag = false;
                this.hasHitLand = true;
                Explosions.Explode10Kg_Land(AerialMine2.P, 2.0F, 2.0F);
                this.destroy();
            }
        }
        if (!flag) {
            this.getSpeed(AerialMine2.spd);
            AerialMine2.spd.z = 0.0D;
            AerialMine2.spd.x = 0.0D;
            AerialMine2.spd.y = 0.0D;
            this.setSpeed(AerialMine2.spd);
            AerialMine2.P.z = 0.0D;
            float af[] = new float[3];
            AerialMine2.Or.getYPR(af);
            AerialMine2.Or.setYPR(af[0], 0.0F, af[2]);
            super.pos.setAbs(AerialMine2.P, AerialMine2.Or);
            super.flags &= 0xffffffbf;
            this.drawing(true);
            Eff3DActor.New(this, null, null, 1.0F, "3DO/Effects/Tracers/533mmTorpedo/wave.eff", -1F);
        }
    }

    public void interpolateTick() {
        float f = Time.tickLenFs();
        super.pos.getAbs(AerialMine2.P);
        if (AerialMine2.P.z <= -0.1D) {
            this.surface();
        }
        if (!this.flow) {
            Ballistics.update(this, super.M, super.S);
        } else {
            this.getSpeed(AerialMine2.spd);
            AerialMine2.spd.scale(0.99D);
            this.setSpeed(AerialMine2.spd);
            super.pos.getAbs(AerialMine2.P);
            super.pos.setAbs(AerialMine2.P, AerialMine2.Or);
            AerialMine2.P.x += AerialMine2.spd.x * f;
            AerialMine2.P.y += AerialMine2.spd.y * f;
            super.pos.setAbs(AerialMine2.P);
            if ((this instanceof BombParaInfluenceMine2) || (this instanceof BombInfluenceMine2)) {
                Actor actor = NearestTargets.getEnemy(0, -1, AerialMine2.P, 20F, 0);
                if ((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric)) {
                    if (Actor.isValid(actor)) {
                        this.sendexplosion();
                    }
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Detonated");
                }
            }
            if (!(this instanceof BombParaInfluenceMine2) && !(this instanceof BombInfluenceMine2)) {
                if ((Time.current() > (this.started + this.travelTime)) || !World.land().isWater(AerialMine2.P.x, AerialMine2.P.y)) {
                    this.sendexplosion();
                }
            }
        }
        this.updateSound();
    }

    private void sendexplosion() {
        MsgCollision.post(Time.current(), this, this.Other, null, this.OtherChunk);
    }

    public void start() {
        Class class1 = this.getClass();
        this.init(Property.floatValue(class1, "kalibr", 1.0F), Property.floatValue(class1, "massa", 1.0F));
        this.started = Time.current();
        this.travelTime = (long) Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
        this.setOwner(super.pos.base(), false, false, false);
        super.pos.setBase(null, null, true);
        super.pos.setAbs(super.pos.getCurrent());
        this.getSpeed(AerialMine2.spd);
        super.pos.getAbs(AerialMine2.P, AerialMine2.Or);
        Vector3d vector3d = new Vector3d(Property.floatValue(class1, "startingspeed", 0.0F), 0.0D, 0.0D);
        AerialMine2.Or.transform(vector3d);
        AerialMine2.spd.add(vector3d);
        this.setSpeed(AerialMine2.spd);
        this.collide(true);
        this.interpPut(new Bomb.Interpolater(), null, Time.current(), null);
        this.drawing(true);
        if (Property.containsValue(class1, "emitColor")) {
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor((Color3f) Property.value(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
            lightpointactor.light.setEmit(Property.floatValue(class1, "emitMax", 1.0F), Property.floatValue(class1, "emitLen", 50F));
            super.draw.lightMap().put("light", lightpointactor);
        }
        super.sound = this.newSound(Property.stringValue(class1, "sound", null), false);
        if (super.sound != null) {
            super.sound.play();
        }
    }

    public AerialMine2() {
        this.hasHitLand = false;
        this.doLandExplosion = false;
    }

    public static void clearInfo() {
        AerialMine2.infoMap.clear();
    }

    public static Map getInfoList() {
        return AerialMine2.infoMap;
    }

    private static boolean isDiffFragileTorps() {
        if (Reflection.getField(World.cur().diffCur, "FragileTorps") == null) {
            return false;
        }
        return Reflection.getBoolean(World.cur().diffCur, "FragileTorps");
    }

    public static void setInfo(Class class1) {
        if ((class1 == null) || BombParaAerialMine2.class.isAssignableFrom(class1)/* || !isDiffFragileTorps()*/) {
            return;
        }
        String s = class1.toString().substring(37);
        if (s.endsWith("L") || s.endsWith("R")) {
            s = s.substring(0, s.length() - 1);
        }
        ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/gui", RTSConf.cur.locale, LDRres.loader());
        String s1 = s;
        String as[] = new String[6];
        try {
            s1 = resourcebundle.getString(s);
        } catch (MissingResourceException missingresourceexception) {
            System.out.println(missingresourceexception);
        }
        as[0] = resourcebundle.getString("AerialMine") + " " + s1;
        try {
            float f = Property.floatValue(class1, "dropAltitude", 0.0F);
            float f1 = Property.floatValue(class1, "dropSpeed", 0.0F);
            as[1] = "  " + (int) f + " " + resourcebundle.getString("DropAltM");
            as[2] = "  " + (int) f1 + " " + resourcebundle.getString("DropSpeedKm");
            as[3] = "  " + (int) (f * 3.28084F) + " " + resourcebundle.getString("DropAltFt");
            as[4] = "  " + (int) (f1 * 0.539957F) + " " + resourcebundle.getString("DropSpeedKnt");
            as[5] = "  " + (int) (f1 * 0.621371F) + " " + resourcebundle.getString("DropSpeedMph");
        } catch (MissingResourceException missingresourceexception1) {
            System.out.println(missingresourceexception1);
        }
        if (!AerialMine2.infoMap.containsKey(s)) {
            AerialMine2.infoMap.put(s, as);
        }
    }

    Actor              Other;
    String             OtherChunk;
    String             ThisChunk;
    boolean            flow;
    private long       travelTime;
    private long       started;
    private float      dropSpeed;
    protected float    dropMinAltitude;
    protected float    dropMaxAltitude;
    boolean            hasHitLand;
    boolean            doLandExplosion;
    private static Map infoMap = new HashMap();
    static Vector3d    spd     = new Vector3d();
    static Orient      Or      = new Orient();
    static Point3d     P       = new Point3d();
}
