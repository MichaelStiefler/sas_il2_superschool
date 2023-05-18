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

public class AerialMine2 extends Bomb
{

    public void msgCollision(Actor actor, String s, String s1)
    {
        Class class1 = getClass();
        armingTime = (long)Property.floatValue(class1, "armingTime", 1.0F) * 1000L;
        Other = actor;
        OtherChunk = s1;
        if(actor instanceof ActorLand)
        {
            if(flow)
            {
                doExplosion(actor, s1);
                return;
            }
            surface();
            if(World.land().isWater(P.x, P.y))
                return;
        }
        if(flow)
        {
         	if ((getOwner() instanceof Aircraft) && !(this instanceof BombParaInfluenceMine2)&& !(this instanceof BombInfluenceMine2))
            {
                Aircraft aircraft1 = (Aircraft)getOwner();
                if(aircraft1.FM.isPlayers())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Hit");
            }
         	if ((getOwner() instanceof Aircraft) && ((this instanceof BombParaInfluenceMine2)|| (this instanceof BombInfluenceMine2)))
            {
                Aircraft aircraft1 = (Aircraft)getOwner();
                if(aircraft1.FM.isPlayers())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Activated");
            }
            doExplosion(actor, s1);
        }
        doExplosion(actor, s1);
    }

    private void surface()
    {
        Class class1 = getClass();
        boolean flag = false;
        travelTime = (long)Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
        dropSpeed = Property.floatValue(class1, "dropSpeed", 1.0F);
        dropMaxAltitude = Property.floatValue(class1, "dropMaxAltitude", 1.0F);
        dropMinAltitude = Property.floatValue(class1, "dropMinAltitude", 1.0F);
        if((this instanceof BombParaInfluenceMine2)||(this instanceof BombParaAerialMine2))
        {
            float f2 = (float)spd.length();
            float f5 = 5F / f2;
            spd.scale(f5);
            setSpeed(spd);
        }
        super.pos.getAbs(P, Or);
        flow = true;
        getSpeed(spd);
        if(World.land().isWater(P.x, P.y))
        {
            if(spd.z < -0.12D)
                Explosions.Explode10Kg_Water(P, 4F, 1.0F);
//                Explosions.torpedoEnter_Water(P, 4F, 1.0F);
            double d = spd.length();
            double dd = d * 3.6D;
            int k = (int)dd;
            String MineSpeed = String.valueOf(k);
            double l = spd.z;
            double ll = l * 3.6D;
            int m = (int)ll;
            String Vspeed = String.valueOf(m);
            double n = dd * dd - ll * ll;
            double nn = Math.sqrt(n);
            int p = (int)nn;
            String Hspeed = String.valueOf(p);
            double h = (l * l) / 19.62D;
            int hh = (int)h;
            String DropAlt = String.valueOf(hh);
            Aircraft aircraft1 = null;
            boolean flag1 = false;
            if((getOwner() instanceof Aircraft) && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && isDiffFragileTorps())
            {
                flag1 = true;
                aircraft1 = (Aircraft)getOwner();
            }
            if(flag1 && (float)p > dropSpeed && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && isDiffFragileTorps())
            {
                if(aircraft1 != null && aircraft1.FM.isPlayers())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Damaged at Entry in Water!");
                HUD.log(AircraftHotKeys.hudLogPowerId, "Dropping Speed Too High!MineSpeed = " + MineSpeed + "Drop Speed = " + Hspeed + " Vertical Speed =" + Vspeed + " Drop Height = " + DropAlt);
                flag = true;
                destroy();
            }
            if(flag1 && (float)hh > dropMaxAltitude && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && isDiffFragileTorps())
            {
                if(aircraft1 != null && aircraft1.FM.isPlayers())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Destroyed at Entry in Water!");
                HUD.log(AircraftHotKeys.hudLogPowerId, "Dropping Altitude Too High!MineSpeed = " + MineSpeed + "Drop Speed = " + Hspeed + " Vertical Speed =" + Vspeed + " Drop Height = " + DropAlt);
                flag = true;
                destroy();
            }
            if(flag1 && (float)hh < dropMinAltitude && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && isDiffFragileTorps())
            {
                if(aircraft1 != null && aircraft1.FM.isPlayers())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Did Not Arm at Entry in Water!");
                HUD.log(AircraftHotKeys.hudLogPowerId, "Dropping Altitude Too Low!MineSpeed = " + MineSpeed + "Drop Speed = " + Hspeed + " Vertical Speed =" + Vspeed + " Drop Height = " + DropAlt);
                flag = true;
                destroy();
            }
            if(flag1 && (float)p < dropSpeed && (float)hh < dropMaxAltitude && (float)hh > dropMinAltitude && !(this instanceof BombParaAerialMine2) && !(this instanceof BombParaInfluenceMine2) && isDiffFragileTorps())
            {
                if(aircraft1 != null && aircraft1.FM.isPlayers())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Good Entry! ");
                flag = false;
            }
        } else
        {
            float f1 = World.Rnd().nextFloat(1.0F, 3F);
            if(f1 <= 2.0F)
            {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Exploded!");
                flag = true;
                hasHitLand = true;
                destroy();
            } else
            {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Dud!");
                flag = false;
                hasHitLand = true;
                Explosions.Explode10Kg_Land(P, 2.0F, 2.0F);
                destroy();
            }
        }
        if(!flag)
        {
            getSpeed(spd);
            spd.z = 0.0D;
            spd.x = 0.0D;
            spd.y = 0.0D;
            setSpeed(spd);
            P.z = 0.0D;
            float af[] = new float[3];
            Or.getYPR(af);
            Or.setYPR(af[0], 0.0F, af[2]);
            super.pos.setAbs(P, Or);
            super.flags &= 0xffffffbf;
            drawing(true);
            Eff3DActor.New(this, null, null, 1.0F, "3DO/Effects/Tracers/533mmTorpedo/wave.eff", -1F);
        }
    }

    public void interpolateTick()
    {
        float f = Time.tickLenFs();
        super.pos.getAbs(P);
        if(P.z <= -0.1D)
            surface();
        if(!flow)
        {
            Ballistics.update(this, super.M, super.S);
        } else
        {
            getSpeed(spd);
            spd.scale(0.99D);
            setSpeed(spd);
            super.pos.getAbs(P);
            super.pos.setAbs(P, Or);
            P.x += spd.x * (double)f;
            P.y += spd.y * (double)f;
            super.pos.setAbs(P);
            if ((this instanceof BombParaInfluenceMine2) || (this instanceof BombInfluenceMine2))
     	        {
            	Actor actor = NearestTargets.getEnemy(0, -1, P, 20F, 0);
                if (actor instanceof ShipGeneric || actor instanceof BigshipGeneric)
                   {
                   if(Actor.isValid(actor))  
                   sendexplosion();
                   HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Detonated");
             	   }
     	        }
            if (!(this instanceof BombParaInfluenceMine2)&& !(this instanceof BombInfluenceMine2)) 
              {
               if(Time.current() > started + travelTime || !World.land().isWater(P.x, P.y))
                sendexplosion();
              }
        }
        updateSound();
    }

    private void sendexplosion()
    {
        MsgCollision.post(Time.current(), this, Other, null, OtherChunk);
    }

    public void start()
    {
        Class class1 = getClass();
        init(Property.floatValue(class1, "kalibr", 1.0F), Property.floatValue(class1, "massa", 1.0F));
        started = Time.current();
        travelTime = (long)Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
        setOwner(super.pos.base(), false, false, false);
        super.pos.setBase(null, null, true);
        super.pos.setAbs(super.pos.getCurrent());
        getSpeed(spd);
        super.pos.getAbs(P, Or);
        Vector3d vector3d = new Vector3d(Property.floatValue(class1, "startingspeed", 0.0F), 0.0D, 0.0D);
        Or.transform(vector3d);
        spd.add(vector3d);
        setSpeed(spd);
        collide(true);
        interpPut(new Bomb.Interpolater(), null, Time.current(), null);
        drawing(true);
        if(Property.containsValue(class1, "emitColor"))
        {
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor((Color3f)Property.value(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
            lightpointactor.light.setEmit(Property.floatValue(class1, "emitMax", 1.0F), Property.floatValue(class1, "emitLen", 50F));
            super.draw.lightMap().put("light", lightpointactor);
        }
        super.sound = newSound(Property.stringValue(class1, "sound", null), false);
        if(super.sound != null)
            super.sound.play();
    }

    public AerialMine2()
    {
        hasHitLand = false;
        doLandExplosion = false;
    }

    public static void clearInfo()
    {
        infoMap.clear();
    }

    public static Map getInfoList()
    {
        return infoMap;
    }
    
    private static boolean isDiffFragileTorps() {
        if (Reflection.getField(World.cur().diffCur, "FragileTorps") == null) return false;
        return Reflection.getBoolean(World.cur().diffCur, "FragileTorps");
    }

    public static void setInfo(Class class1)
    {
        if(class1 == null || BombParaAerialMine2.class.isAssignableFrom(class1)/* || !isDiffFragileTorps()*/)
            return;
        String s = class1.toString().substring(37);
        if(s.endsWith("L") || s.endsWith("R"))
            s = s.substring(0, s.length() - 1);
        ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/gui", RTSConf.cur.locale, LDRres.loader());
        String s1 = s;
        String as[] = new String[6];
        try
        {
            s1 = resourcebundle.getString(s);
        }
        catch(MissingResourceException missingresourceexception)
        {
            System.out.println(missingresourceexception);
        }
        as[0] = resourcebundle.getString("AerialMine") + " " + s1;
        try
        {
            float f = Property.floatValue(class1, "dropAltitude", 0.0F);
            float f1 = Property.floatValue(class1, "dropSpeed", 0.0F);
            as[1] = "  " + (int)f + " " + resourcebundle.getString("DropAltM");
            as[2] = "  " + (int)f1 + " " + resourcebundle.getString("DropSpeedKm");
            as[3] = "  " + (int)(f * 3.28084F) + " " + resourcebundle.getString("DropAltFt");
            as[4] = "  " + (int)(f1 * 0.539957F) + " " + resourcebundle.getString("DropSpeedKnt");
            as[5] = "  " + (int)(f1 * 0.621371F) + " " + resourcebundle.getString("DropSpeedMph");
        }
        catch(MissingResourceException missingresourceexception1)
        {
            System.out.println(missingresourceexception1);
        }
        if(!infoMap.containsKey(s))
            infoMap.put(s, as);
    }

    Actor Other;
    String OtherChunk;
    String ThisChunk;
    boolean flow;
    private long travelTime;
    private long started;
    private float dropSpeed;
    protected float dropMinAltitude;
    protected float dropMaxAltitude;
    boolean hasHitLand;
    boolean doLandExplosion;
    private static Map infoMap = new HashMap();
    static Vector3d spd = new Vector3d();
    static Orient Or = new Orient();
    static Point3d P = new Point3d();
}