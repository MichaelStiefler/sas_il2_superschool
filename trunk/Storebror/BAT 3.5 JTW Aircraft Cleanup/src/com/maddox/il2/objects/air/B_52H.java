package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class B_52H extends B_52fuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public B_52H()
    {
        fxSirena = newSound("aircraft.F4warning", false);
        smplSirena = new Sample("sample.F4warning.wav", 256, 65535);
        sirenaSoundPlaying = false;
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
        error = 0;
        guidedMissileUtils = new GuidedMissileUtils(this);
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "H_";
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        super.update(f);
        guidedMissileUtils.update();
        pullUP();
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            for(int i = 0; i < 8; i++)
                if(this.FM.EI.engines[i].getPowerOutput() > 0.8F && this.FM.EI.engines[i].getStage() == 6)
                {
                    if(this.FM.EI.engines[i].getPowerOutput() > 0.95F)
                        this.FM.AS.setSootState(this, i, 3);
                    else
                        this.FM.AS.setSootState(this, i, 2);
                } else
                {
                    this.FM.AS.setSootState(this, i, 0);
                }

        }
    }

    public void pullUP()
    {
        float f = FM.getAltitude() - (float)Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y);
        if(f < 600F && this.FM.getSpeedKMH() < 400F && FM.getVertSpeed() < -40F || f < 100F && FM.CT.getGear() < 0.999999F)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "!!PULL UP!!");
        } else
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public void playSirenaWarning(boolean flag)
    {
        if(flag && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "!!PULL UP!!");
        } else
        if(!flag && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        if(raretimer != Time.current() && this == World.getPlayerAircraft())
        {
            counter++;
            if(counter++ % 5 == 0)
                GroundRadar();
            if(counter++ % 5 == 0)
                NavyRadar();
        }
        super.rareAction(f, flag);
        raretimer = Time.current();
    }

    private boolean GroundRadar()
    {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(((actor instanceof ArtilleryGeneric) || (actor instanceof Wagon) || (actor instanceof TankGeneric) || (actor instanceof CarGeneric) || (actor instanceof PlaneGeneric)) && actor != World.getPlayerAircraft())
            {
                this.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                new String();
                new String();
                int k = (int)(Math.floor(((Tuple3d) (actor.pos.getAbsPoint())).z * 0.10000000000000001D) * 10D);
                int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 6000D) * 10D);
                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d8, -d7);
                int i1 = (int)(Math.floor((int)f) - 90D);
                if(i1 < 0)
                    i1 += 360;
                int j1 = i1 - i;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.sqrt(d6 * d6);
                int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                int l1 = (int)(Math.floor((int)f1) - 90D);
                if(l1 < 0)
                    l1 += 360;
                int i2 = l1 - j;
                k1 = (int)((double)k1 / 1000D);
                int j2 = (int)Math.ceil(k1);
                String s = "Contact, ";
                String s1 = "Unknown, ";
                byte byte0 = 9;
                if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 20D)
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Ground units bearing " + i1 + "\260" + ", range " + j2 + " km");
            }
        }

        return true;
    }

    private boolean NavyRadar()
    {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor != World.getPlayerAircraft())
            {
                this.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                new String();
                new String();
                int k = (int)(Math.floor(((Tuple3d) (actor.pos.getAbsPoint())).z * 0.10000000000000001D) * 10D);
                int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d8, -d7);
                int i1 = (int)(Math.floor((int)f) - 90D);
                if(i1 < 0)
                    i1 += 360;
                int j1 = i1 - i;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.sqrt(d6 * d6);
                int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                int l1 = (int)(Math.floor((int)f1) - 90D);
                if(l1 < 0)
                    l1 += 360;
                int i2 = l1 - j;
                k1 = (int)((double)k1 / 1000D);
                int j2 = (int)Math.ceil(k1);
                String s = "Contact, ";
                String s1 = "Unknown, ";
                byte byte0 = 9;
                if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))
                    byte0 = 100;
                s1 = "surface unit, ";
                if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 20D)
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "                         " + s + s1 + " bearing " + i1 + "\260" + ", range " + j2 + " km");
            }
        }

        return true;
    }

    public void setTimer(int i)
    {
        Random random = new Random();
        Timer1 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
        Timer2 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
    }

    public void resetTimer(float f)
    {
        Timer1 = f;
        Timer2 = f;
    }

    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    public float Timer1;
    public float Timer2;
    private int freq;
    private int counter;
    private int error;
    private long raretimer;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;

    static 
    {
        Class class1 = B_52H.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-52");
        Property.set(class1, "meshName", "3DO/Plane/B52D(Multi1)/hier_H.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/B-52H.fmd:B52FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitB_52.class, CockpitB_52Bombardier.class, CockpitB_52DGunner.class
        });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 10, 10, 10, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", 
            "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", 
            "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", 
            "_BombSpawn27", "_BombSpawn201", "_BombSpawn202", "_BombSpawn203", "_BombSpawn204", "_BombSpawn205", "_BombSpawn206", "_BombSpawn207", "_BombSpawn208", "_BombSpawn209", 
            "_BombSpawn210", "_BombSpawn211", "_BombSpawn212", "_BombSpawn213", "_BombSpawn214", "_BombSpawn215", "_BombSpawn216", "_BombSpawn217", "_BombSpawn218", "_BombSpawn219", 
            "_BombSpawn220", "_BombSpawn221", "_BombSpawn222", "_BombSpawn223", "_BombSpawn224", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", 
            "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_BombSpawn225", "_BombSpawn226", "_BombSpawn227", "_BombSpawn228", "_BombSpawn229", 
            "_BombSpawn230", "_BombSpawn231", "_BombSpawn232", "_BombSpawn233", "_BombSpawn234", "_BombSpawn235", "_BombSpawn236", "_BombSpawn237", "_BombSpawn238", "_BombSpawn239", 
            "_BombSpawn240", "_BombSpawn241", "_BombSpawn242", "_BombSpawn28", "_BombSpawn29", "_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", 
            "_BombSpawn35", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", 
            "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_Rock17", "_Rock18"
        });
    }
}
